package cena;

import java.awt.Color;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;

import textura.Textura;

public class Design {

    public static void design(GLAutoDrawable drawable, float xMin, float xMax, float yMin, float yMax, Textura textura,
            int paddle1Y, int paddle2Y, int paddleHeight, int paddleWidth, int ballSize, Ball ball, int player1Score,
            int computer, TextRenderer textRenderer, int ballX, int ballY, int playerLives) {
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
        drawPaddle(gl, -95, paddle1Y, paddleWidth, paddleHeight, textura);
        drawPaddle(gl, 95 - paddleWidth, paddle2Y, paddleWidth, paddleHeight, textura);

        // Desenha a bolinha
        drawBall(gl, ballX, ballY, ballSize, ball);

        // Desenha o placar
        String scoreText = player1Score + " | " + computer;
        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        textRenderer.setColor(Color.WHITE);
        textRenderer.draw(scoreText, (drawable.getSurfaceWidth() / 2) - 30, drawable.getSurfaceHeight() - 30);

        // Desenha as vidas do jogador
        String livesText = "Vidas: ";
        for (int i = 0; i < playerLives; i++) {
            livesText += "\u2665 "; 
        }
        textRenderer.draw(livesText, 10, drawable.getSurfaceHeight() - 30);

        textRenderer.endRendering();
    }

    public static void designPhaseTwo(GLAutoDrawable drawable, float xMin, float xMax, float yMin, float yMax, Textura textura,
            int paddle1Y, int paddle2Y, int paddleHeight, int paddleWidth, int ballSize, Ball ball, int player1Score,
            int computer, TextRenderer textRenderer, int ballX, int ballY, int playerLives) {
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

        // Desenha os obstáculos centralizados no eixo X
        gl.glPushMatrix();
        gl.glColor3f(1, 1, 1); // Branco
        // Primeiro obstáculo em (0, 20) com tamanho (10, 10)
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-5, 20);
        gl.glVertex2f(5, 20);
        gl.glVertex2f(5, 30);
        gl.glVertex2f(-5, 30);
        gl.glEnd();
        // Segundo obstáculo em (0, -20) com tamanho (10, 10)
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-5, -30);
        gl.glVertex2f(5, -30);
        gl.glVertex2f(5, -20);
        gl.glVertex2f(-5, -20);
        gl.glEnd();
        gl.glPopMatrix();

        // Desenha as raquetes
        drawPaddle(gl, -95, paddle1Y, paddleWidth, paddleHeight, textura);
        drawPaddle(gl, 95 - paddleWidth, paddle2Y, paddleWidth, paddleHeight, textura);

        // Desenha a bolinha
        drawBall(gl, ballX, ballY, ballSize, ball);

        // Desenha o placar
        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        textRenderer.setColor(Color.WHITE);
        textRenderer.draw(player1Score + " | " + computer, (drawable.getSurfaceWidth() / 2) - 30, drawable.getSurfaceHeight() - 30);

        // Desenha as vidas do jogador
        String livesText = "Vidas: ";
        for (int i = 0; i < playerLives; i++) {
            livesText += "\u2665 ";
        }
        textRenderer.draw(livesText, 10, drawable.getSurfaceHeight() - 50);

        textRenderer.endRendering();
    }

    private static void drawPaddle(GL2 gl, int x, int paddleY, int paddleWidth, int paddleHeight, Textura textura) {
        gl.glEnable(GL2.GL_TEXTURE_2D);
        textura.vetTextures[1].bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(x, paddleY - paddleHeight / 2);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(x + paddleWidth, paddleY - paddleHeight / 2);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(x + paddleWidth, paddleY + paddleHeight / 2);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(x, paddleY + paddleHeight / 2);
        gl.glEnd();
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }

    private static void drawBall(GL2 gl, int ballX, int ballY, int ballSize, Ball ball) {
        gl.glPushMatrix();
        gl.glTranslatef(ballX, ballY, 0);
        ball.drawSphere(gl, 0, 0, ballSize, 20, 20);
        gl.glPopMatrix();
    }
}
