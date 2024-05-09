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
			int computer, TextRenderer textRenderer, int ballX, int ballY) {
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
	}

	public static void designPhaseTwo(GLAutoDrawable drawable, float xMin, float xMax, float yMin, float yMax, Textura textura,
			int paddle1Y, int paddle2Y, int paddleHeight, int paddleWidth, int ballSize, Ball ball, int player1Score,
			int computer, TextRenderer textRenderer, int ballX, int ballY) {
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

 // Desenha os obstáculos com as correções
    gl.glPushMatrix();
    gl.glColor3f(1, 1, 1); // Branco
    // Primeiro obstáculo em (-30, 20) com tamanho (10, 10)
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2f(-30, 20);
    gl.glVertex2f(-20, 20);
    gl.glVertex2f(-20, 30);
    gl.glVertex2f(-30, 30);
    gl.glEnd();
    // Segundo obstáculo em (20, -30) com tamanho (10, 10)
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2f(20, -30);
    gl.glVertex2f(30, -30);
    gl.glVertex2f(30, -20);
    gl.glVertex2f(20, -20);
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

	private static void drawTriangleObstacle(GL2 gl) {
		gl.glColor3f(0.3f, 0.3f, 0.3f); // Define a cor do triângulo
		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glVertex2f(-10, 0); // Ponto inferior esquerdo
		gl.glVertex2f(10, 0); // Ponto inferior direito
		gl.glVertex2f(0, 30); // Ponto superior central
		gl.glEnd();
	}

}
