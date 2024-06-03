package cena;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;

import textura.Textura;

public class Design {

    public static void design(GLAutoDrawable drawable, float xMin, float xMax, float yMin, float yMax, Textura textura,
            int paddle1Y, int paddle2Y, int paddleHeight, int paddleWidth, int ballSize, int player1Score,
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
        drawBall(gl, ballX, ballY, ballSize);

        // Desenha o placar
        String scoreText = player1Score + " | " + computer;
        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        textRenderer.setColor(java.awt.Color.WHITE);
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
            int paddle1Y, int paddle2Y, int paddleHeight, int paddleWidth, int ballSize, int player1Score,
            int computer, TextRenderer textRenderer, int ballX, int ballY, int playerLives, int obstacle1X, int obstacle2X) {
        GL2 gl = drawable.getGL().getGL2();

        // Limpa a tela
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // Reseta as transformações
        gl.glLoadIdentity();
        // Define a projeção ortográfica com base nos limites do campo
        gl.glOrtho(xMin, xMax, yMin, yMax, -1, 1);

        // Desenha o fundo com a textura
        gl.glEnable(GL2.GL_TEXTURE_2D);
        textura.vetTextures[2].bind(gl);
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
        // Primeiro obstáculo com posição variável no eixo X
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(obstacle1X - 5, 20);
        gl.glVertex2f(obstacle1X + 5, 20);
        gl.glVertex2f(obstacle1X + 5, 30);
        gl.glVertex2f(obstacle1X - 5, 30);
        gl.glEnd();
        // Segundo obstáculo com posição variável no eixo X
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(obstacle2X - 5, -30);
        gl.glVertex2f(obstacle2X + 5, -30);
        gl.glVertex2f(obstacle2X + 5, -20);
        gl.glVertex2f(obstacle2X - 5, -20);
        gl.glEnd();
        gl.glPopMatrix();

        // Desenha as raquetes
        drawPaddle(gl, -95, paddle1Y, paddleWidth, paddleHeight, textura);
        drawPaddle(gl, 95 - paddleWidth, paddle2Y, paddleWidth, paddleHeight, textura);

        // Desenha a bolinha
        drawBall(gl, ballX, ballY, ballSize);

        // Desenha o placar
        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        textRenderer.setColor(java.awt.Color.WHITE);
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

        // Configurar material da raquete
        float[] materialAmbient = { 0.3f, 0.3f, 0.3f, 1.0f };  // Aumentar intensidade
        float[] materialDiffuse = { 0.3f, 0.8f, 0.3f, 1.0f };  // Aumentar intensidade
        float[] materialSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
        float materialShininess = 50.0f;

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, materialAmbient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, materialDiffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, materialShininess);

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

    private static void drawBall(GL2 gl, int ballX, int ballY, int ballSize) {
        gl.glPushMatrix();
        gl.glTranslatef(ballX, ballY, 0);

        // Configurar material da bola
        float[] materialAmbient = { 0.3f, 0.3f, 0.3f, 1.0f };  // Aumentar intensidade
        float[] materialDiffuse = { 0.8f, 0.3f, 0.3f, 1.0f };  // Aumentar intensidade
        float[] materialSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
        float materialShininess = 50.0f;

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, materialAmbient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, materialDiffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, materialShininess);

        drawSphere(gl, 0, 0, ballSize, 20, 20);
        gl.glPopMatrix();
    }

    // Método para desenhar a esfera (bola) usando triângulos
    private static void drawSphere(GL2 gl, float centerX, float centerY, float radius, int slices, int stacks) {
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        drawCircle(gl, centerX, centerY, radius / 2, slices);
    }

    // Método para desenhar um círculo que representa a bola
    private static void drawCircle(GL2 gl, float centerX, float centerY, float radius, int numSegments) {
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex2f(centerX, centerY); // Centro do círculo

        for (int i = 0; i <= numSegments; i++) {
            double angle = 2.0 * Math.PI * i / numSegments;
            float x = centerX + (float) (radius * Math.cos(angle));
            float y = centerY + (float) (radius * Math.sin(angle));
            gl.glVertex2f(x, y);
        }

        gl.glEnd();
    }
}
