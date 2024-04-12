package cena;

import static validations.Collisions.collisionPaddleWall;
import static validations.Collisions.collisionWallBall;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;

import textura.Textura;

public class Cena implements GLEventListener{
    // Variáveis para definir os limites do campo de jogo
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    // Dimensões das raquetes
    private int paddleWidth = 5;
    private int paddleHeight = 50;
    // Posição inicial das raquetes
    public int paddle1Y = 0;
    public int paddle2Y = 0;
    // Características da bola
    private int ballSize = 10;
    private int ballX = 0;
    private int ballY = 0;
    private int ballDX = 2; // Velocidade horizontal da bola
    private int ballDY = 2; // Velocidade vertical da bola
    private int player1Score = 0;
    private int computer = 0;
    // Renderizador de texto para o placar
    private TextRenderer textRenderer;
    // Objeto GLU para funções auxiliares
    private GLU glu;
    // Gerador de números aleatórios
    private Random rand = new Random();
    private Ball ball = new Ball();
    private Textura textura;

    public Cena() {
        // Define os limites do campo de jogo
        xMin = -100;
        xMax = 100;
        yMin = -100;
        yMax = 100;

        // Posiciona a bola no centro do campo
        ballX = (int) ((xMax - xMin) / 2);
        ballY = (int) ((yMax - yMin) / 2);

        textRenderer = new TextRenderer(new Font("Arial", Font.BOLD, 24));
        glu = new GLU();
        
        // Inicialize o objeto de textura
        textura = new Textura(3); 	
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        
        textura.gerarTextura(gl, "imagens/planoDeFundo.jpg", 0);
        textura.gerarTextura(gl, "imagens/raquetes.jpg", 1);
        textura.gerarTextura(gl, "imagens/bola.jpg", 2);
    }

    // Método chamado para cada quadro de animação, onde ocorre o desenho do jogo
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        // Limpa a tela
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // Reseta as transformações
        gl.glLoadIdentity();
        // Define a projeção ortográfica com base nos limites do campo
        gl.glOrtho(xMin, xMax, yMin, yMax, -1, 1);

        // Desenha o fundo com a textura
        gl.glEnable(GL2.GL_TEXTURE_2D);
        textura.vetTextures[0].bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(xMin, yMin);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(xMax, yMin);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(xMax, yMax);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(xMin, yMax);
        gl.glEnd();
        gl.glDisable(GL2.GL_TEXTURE_2D);

        // Desenha as raquetes
        gl.glRecti(-95, paddle1Y - paddleHeight / 2, -95 + paddleWidth, paddle1Y + paddleHeight / 2);
        gl.glRecti(95 - paddleWidth, paddle2Y - paddleHeight / 2, 95, paddle2Y + paddleHeight / 2);

        gl.glEnable(GL2.GL_TEXTURE_2D);
        textura.vetTextures[1].bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(-95, paddle1Y - paddleHeight / 2);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(-95 + paddleWidth, paddle1Y - paddleHeight / 2);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(-95 + paddleWidth, paddle1Y + paddleHeight / 2);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(-95, paddle1Y + paddleHeight / 2);
        gl.glEnd();
        
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(95 - paddleWidth, paddle2Y - paddleHeight / 2);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(95, paddle2Y - paddleHeight / 2);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(95, paddle2Y + paddleHeight / 2);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(95 - paddleWidth, paddle2Y + paddleHeight / 2);
        gl.glEnd();
        gl.glDisable(GL2.GL_TEXTURE_2D);
        
        // Desenha a bolinha
        gl.glPushMatrix();
        float ballCenterX = (float) ballX; // Coordenada X do centro da bola
        float ballCenterY = (float) ballY; // Coordenada Y do centro da bola
        gl.glTranslatef(ballCenterX, ballCenterY, 0);
        ball.drawSphere(gl, 0, 0, ballSize, 20, 20); // Passa as coordenadas do centro da bola
        gl.glPopMatrix();

        // Desenha o placar
        String scoreText = player1Score + " | " + computer;
        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        textRenderer.setColor(Color.WHITE);
        textRenderer.draw(scoreText, (drawable.getSurfaceWidth() / 2) - 30, drawable.getSurfaceHeight() - 30);
        textRenderer.endRendering();

        // Atualiza o estado do jogo
        update();
    }

    private void update() {
        // Movimenta automaticamente a raquete do computador
        if (ballX > 0) {
            if (ballY > paddle2Y) {
                paddle2Y += 1;
            } else if (ballY < paddle2Y) {
                paddle2Y -= 1;
            }
        }

        // Atualiza a posição da bola
        ballX += ballDX;
        ballY += ballDY;

        // Verifica colisão com as paredes superior e inferior
        ballDY = collisionWallBall(ballY, ballSize, yMax, yMin, ballDY);
        
        // Verifica colisão das raquetes com as paredes superior e inferior
        paddle1Y = collisionPaddleWall(paddle1Y, paddleHeight, yMax, yMin);
        paddle2Y = collisionPaddleWall(paddle2Y, paddleHeight, yMax, yMin);
        
        // Verifica colisão com as raquetes e adiciona variação aleatória na direção vertical da bola
        if ((ballX - ballSize / 2 <= -95 + paddleWidth) && (ballY >= paddle1Y - paddleHeight / 2 && ballY <= paddle1Y + paddleHeight / 2)) {
            ballDX *= -1;
            ballDY += rand.nextInt(3) - 1; // Variação de -1 a 1
        } else if ((ballX + ballSize / 2 >= 95 - paddleWidth) && (ballY >= paddle2Y - paddleHeight / 2 && ballY <= paddle2Y + paddleHeight / 2)) {
            ballDX *= -1;
            ballDY += rand.nextInt(3) - 1; // Variação de -1 a 1
        } 
        
        // Verifica se a bola saiu do campo e atualiza o placar
        if (ballX + ballSize / 2 >= xMax) {
            player1Score++;
            resetBall();
        } else if (ballX - ballSize / 2 <= xMin) {
            computer++;
            resetBall();
        }
        
    }

    private void resetBall() {
    	ballX = 0;
        ballY = 0;
        ballDX = rand.nextBoolean() ? 2 : -2; // Define direção horizontal aleatória
        ballDY = rand.nextInt(5) - 2; // Define direção vertical aleatória entre -2 e 2
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        // Evita a divisão por zero
        if (height == 0) height = 1;

        // Define a área de visualização considerando a correção de aspecto
        if (width > height) {
            // Maior largura, ajusta a altura para manter a proporção
            float aspectRatio = (float) height / width;
            yMin = -100 * aspectRatio;
            yMax = 100 * aspectRatio;
            paddleHeight = (int) (80 * aspectRatio); // Ajusta a altura das raquetes
            ballSize = (int) (10 * aspectRatio); // Ajusta o tamanho da bola
        } else {
            // Maior altura, ajusta a largura para manter a proporção
            float aspectRatio = (float) width / height;
            xMin = -100 * aspectRatio;
            xMax = 100 * aspectRatio;
            paddleWidth = (int) (10 * aspectRatio); // Ajusta a largura das raquetes
            ballSize = (int) (10 * aspectRatio); // Ajusta o tamanho da bola
        }

        // Define a viewport para abranger a janela inteira
        gl.glViewport(0, 0, width, height);

        // Ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Projecao ortogonal com correção de aspecto
        gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);

        // Ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

}