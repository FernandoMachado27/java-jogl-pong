package cena;

import static cena.Collisions.checkCollisionWithObstacle;
import static cena.Collisions.checkCollisionWithPaddle;
import static cena.Collisions.collisionPaddleWall;
import static cena.Collisions.collisionWallBall;
import static cena.Design.design;
import static cena.Design.designPhaseTwo;
import static cena.Menu.menu;
import static cena.Menu.menuPhaseTwo;

import java.awt.Font;
import java.util.Random;

import javax.swing.JOptionPane;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;

import textura.Textura;

public class Cena implements GLEventListener {
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
    // Gerador de números aleatórios
    private Random rand = new Random();
    private Textura textura;
    int op;
    private int menuChoice;
    private int phase;
    private boolean isPhaseTwoStarted = false;  
    private boolean isPaused = false; 
    private int playerLives = 5;
    private int obstacle1X = 0;
    private int obstacle2X = 0;
    private int obstacle1DX = 2;
    private int obstacle2DX = -2;
    private final int obstacleLimitXMin = -70;
    private final int obstacleLimitXMax = 70;

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

        textura = new Textura(3);

        player1Score = -40;
        computer = 0;

        menuChoice = menu();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        
        textura.gerarTextura(gl, "imagens/step1.jpg", 0);
        textura.gerarTextura(gl, "imagens/raquetes.jpg", 1);
        textura.gerarTextura(gl, "imagens/step2.jpg", 2);

        // Habilitar iluminação
        gl.glEnable(GLLightingFunc.GL_LIGHTING);
        gl.glEnable(GLLightingFunc.GL_LIGHT0);

        // Configurar a luz
        float[] lightAmbient = { 0.5f, 0.5f, 0.5f, 1.0f };  // Aumentar intensidade
        float[] lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] lightSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] lightPosition = { 0.0f, 0.0f, 1.0f, 0.0f };

        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_SPECULAR, lightSpecular, 0);
        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, lightPosition, 0);

        // Habilitar material
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        if (!isPaused) { 
            if (menuChoice == JOptionPane.YES_OPTION) {
                if (!isPhaseTwoStarted || phase == 1) {
                    design(drawable, xMin, xMax, yMin, yMax, textura, paddle1Y, paddle2Y, paddleHeight, paddleWidth, ballSize, player1Score, computer, textRenderer, ballX, ballY, playerLives);
                }
                if (player1Score >= 200 && !isPhaseTwoStarted) {
                    isPhaseTwoStarted = true; // Marca a fase dois como iniciada
                    menuChoice = menuPhaseTwo();
                    if (menuChoice == JOptionPane.YES_OPTION) {
                        phase = 2; 
                        player1Score = 0; 
                        computer = 0;
                        playerLives = 5; 
                    } else {
                        System.exit(0);
                    }
                }
                if (phase == 2) {
                	designPhaseTwo(drawable, xMin, xMax, yMin, yMax, textura, paddle1Y, paddle2Y, paddleHeight, paddleWidth, ballSize, player1Score, computer, textRenderer, ballX, ballY, playerLives, obstacle1X, obstacle2X);
                }
            } else {
                System.exit(0);
            }

            if (computer >= 200) {
                int response = JOptionPane.showConfirmDialog(null, "O computador venceu, deseja jogar novamente?", "Fim de Jogo", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    resetGame();
                } else {
                    System.exit(0);
                }
            }

            update();
        } else {
            renderPauseMessage(drawable);
        }
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

        // Atualiza a posição dos obstáculos
        obstacle1X += obstacle1DX;
        obstacle2X += obstacle2DX;

        // Verifica colisão dos obstáculos com os novos limites do campo de jogo
        if (obstacle1X + 5 >= obstacleLimitXMax || obstacle1X - 5 <= obstacleLimitXMin) {
            obstacle1DX *= -1;
        }
        if (obstacle2X + 5 >= obstacleLimitXMax || obstacle2X - 5 <= obstacleLimitXMin) {
            obstacle2DX *= -1;
        }

        // Verifica colisão com as paredes superior e inferior
        ballDY = collisionWallBall(ballY, ballSize, yMax, yMin, ballDY);

        // Verifica colisão das raquetes com as paredes superior e inferior
        paddle1Y = collisionPaddleWall(paddle1Y, paddleHeight, yMax, yMin);
        paddle2Y = collisionPaddleWall(paddle2Y, paddleHeight, yMax, yMin);

        // Verifica colisão com as raquetes
        boolean ballHitLeftPaddle = (ballX - ballSize / 2 <= -95 + paddleWidth);
        boolean ballHitRightPaddle = (ballX + ballSize / 2 >= 95 - paddleWidth);

        // Verifica colisão com a raquete esquerda
        if (ballHitLeftPaddle && checkCollisionWithPaddle(ballX, ballY, ballSize, -95 + paddleWidth, paddle1Y, paddleWidth, paddleHeight, true)) {
            if (ballY <= paddle1Y - paddleHeight / 2 + ballSize / 2 || ballY >= paddle1Y + paddleHeight / 2 - ballSize / 2) {
                // Bola encostou nas bordas superior ou inferior da raquete esquerda
                ballDX *= -1;  // Rebater
            } else {
                ballDX *= -1;
                if (phase == 2) {
                    ballDY += rand.nextInt(3) - 1; // Variação de -1 a 1 na fase 2
                }
            }
        }
        // Verifica colisão com a raquete direita
        else if (ballHitRightPaddle && checkCollisionWithPaddle(ballX, ballY, ballSize, 95 - paddleWidth, paddle2Y, paddleWidth, paddleHeight, false)) {
            if (ballY <= paddle2Y - paddleHeight / 2 + ballSize / 2 || ballY >= paddle2Y + paddleHeight / 2 - ballSize / 2) {
                // Bola encostou nas bordas superior ou inferior da raquete direita
                ballDX *= -1;  // Rebater
            } else {
                ballDX *= -1;
                if (phase == 2) {
                    ballDY += rand.nextInt(3) - 1; // Variação de -1 a 1 na fase 2
                }
            }
        }

        // Verifica colisão com os obstáculos apenas na fase 2
        if (phase == 2) {
            boolean collided = false;
            if (checkCollisionWithObstacle(ballX, ballY, ballSize, obstacle1X, 25, 10, 10)) {
                ballDX *= -1;
                ballDY *= -1;
                collided = true;
            }
            if (checkCollisionWithObstacle(ballX, ballY, ballSize, obstacle2X, -25, 10, 10)) {
                ballDX *= -1;
                ballDY *= -1;
                collided = true;
            }
            if (collided) {
                ballX += 2 * ballDX;
                ballY += 2 * ballDY;
            }
        }

        // Verifica se a bola saiu do campo e atualiza o placar
        if (ballX + ballSize / 2 >= xMax || ballX - ballSize / 2 <= xMin) {
            if (ballX + ballSize / 2 >= xMax) {
                player1Score += 40;
            } else {
                computer += 40;
                if (computer % 40 == 0) {
                    playerLives--;
                    if (playerLives == 0) {
                        int response = Menu.menuLoseAllLives();
                        if (response == JOptionPane.YES_OPTION) {
                            resetGame();
                        } else {
                            System.exit(0);
                        }
                    }
                }
            }
            resetBall();  // Reseta a posição da bola
        }

        // Condições para exibir mensagem de vitória ou derrota na fase 2
        if (phase == 2 && (player1Score >= 200 || computer >= 200)) {
            if (player1Score >= 200) {
                int response = Menu.menuWin();
                if (response == JOptionPane.YES_OPTION) {
                    resetGame();
                } else {
                    System.exit(0);
                }
            } else if (computer >= 200) {
                int response = Menu.menuComputerWin();
                if (response == JOptionPane.YES_OPTION) {
                    resetGame();
                } else {
                    System.exit(0);
                }
            }
        }
    }

    private void renderPauseMessage(GLAutoDrawable drawable) {
        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f); // Define a cor do texto para vermelho
        textRenderer.draw("Jogo pausado", drawable.getSurfaceWidth() / 2 - 70, drawable.getSurfaceHeight() / 2); // Renderiza o texto no centro da tela
        textRenderer.endRendering();
    }

    public void resetBall() {
        ballX = 0;  // Centraliza a bola horizontalmente
        ballY = 0;  // Centraliza a bola verticalmente

        if (phase == 2) {
            ballDX = rand.nextBoolean() ? 2 : -2; // Define direção horizontal aleatória (para direita ou para esquerda)
            ballDY = 0; // Sem movimento vertical para evitar obstáculos
        } else {
            ballDX = rand.nextBoolean() ? 2 : -2; // Define direção horizontal aleatória
            ballDY = rand.nextInt(5) - 2; // Define direção vertical aleatória entre -2 e 2

            // Garante que ballDY não seja zero
            while (ballDY == 0) {
                ballDY = rand.nextInt(5) - 2;
            }
        }
    }

    private void resetGame() {
        phase = 1;
        isPhaseTwoStarted = false;
        player1Score = 0;
        computer = 0;
        playerLives = 5; // Reinicia as vidas do jogador
        ballX = 0;
        ballY = 0;
        ballDX = 2;
        ballDY = 2;
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

    public void togglePause() {
        isPaused = !isPaused; // Alterna o estado de pausa
    }
}
