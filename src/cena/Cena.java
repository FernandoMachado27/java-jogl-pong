package cena;

import static cena.Collisions.collisionPaddleWall;
import static cena.Collisions.collisionWallBall;
import static cena.Design.design;
import static cena.Menu.menu;

import java.awt.Font;
import java.util.Random;

import javax.swing.JOptionPane;

import com.jogamp.newt.opengl.GLWindow;
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
    int op;
	private int menuChoice;
	private static GLWindow window = null;

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
        
        player1Score = 0;
        computer = 0;
        
        // Exibe o menu apenas uma vez quando a cena é criada
        menuChoice = menu();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        
        textura.gerarTextura(gl, "imagens/planoDeFundo.jpg", 0);
        textura.gerarTextura(gl, "imagens/raquetes.jpg", 1);
        textura.gerarTextura(gl, "imagens/bola.jpg", 2);
    }

    
    @Override
    public void display(GLAutoDrawable drawable) {
    	
    	if (menuChoice == JOptionPane.YES_OPTION) {
    		design(drawable, xMin, xMax, yMin, yMax, textura, paddle1Y, paddle2Y, paddleHeight, paddleWidth, ballSize, ball, player1Score, computer, textRenderer, ballX, ballY);
		} else {
			System.exit(0);
		}
    	
    	// Método chamado para cada quadro de animação, onde ocorre o desenho do jogo
    	//design(drawable, xMin, xMax, yMin, yMax, textura, paddle1Y, paddle2Y, paddleHeight, paddleWidth, ballSize, ball, player1Score, computer, textRenderer, ballX, ballY);

    	// Desenha o triângulo se o player1Score for igual a 2
        if (player1Score >= 2) {
            GL2 gl = drawable.getGL().getGL2();
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex2f(0, 20); // Ponto superior
            gl.glVertex2f(-20, -20); // Ponto inferior esquerdo
            gl.glVertex2f(20, -20); // Ponto inferior direito
            gl.glEnd();
        }
    	
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