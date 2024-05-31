package cena;

public class Collisions {

    public static int collisionWallBall(int ballY, int ballSize, float yMax, float yMin, int ballDY) {
        if (ballY + ballSize / 2 >= yMax || ballY - ballSize / 2 <= yMin) {
            return -ballDY; // Retorna o valor invertido de ballDY
        }
        return ballDY; // Retorna ballDY sem modificar
    }

    public static int collisionPaddleWall(int paddleY, int paddleHeight, float yMax, float yMin) {
        if (paddleY + paddleHeight / 2 >= yMax) {
            return (int) (yMax - paddleHeight / 2);
        } else if (paddleY - paddleHeight / 2 <= yMin) {
            return (int) (yMin + paddleHeight / 2);
        }
        return paddleY; // Retorna paddleY sem modificar
    }

    // Método para verificar colisão com um obstáculo retangular
    public static boolean checkCollisionWithObstacle(int ballX, int ballY, int ballSize, int obsCenterX, int obsCenterY, int width, int height) {
        int halfBallSize = ballSize / 2;
        int ballLeft = ballX - halfBallSize;
        int ballRight = ballX + halfBallSize;
        int ballTop = ballY + halfBallSize;
        int ballBottom = ballY - halfBallSize;

        int obsLeft = obsCenterX - width / 2;
        int obsRight = obsCenterX + width / 2;
        int obsTop = obsCenterY + height / 2;
        int obsBottom = obsCenterY - height / 2;

        // Verifica se há sobreposição nos eixos x e y
        boolean overlapX = (ballLeft < obsRight) && (ballRight > obsLeft);
        boolean overlapY = (ballBottom < obsTop) && (ballTop > obsBottom);

        return overlapX && overlapY;
    }

    // Método para verificar colisão com as raquetes
    public static boolean checkCollisionWithPaddle(int ballX, int ballY, int ballSize, int paddleX, int paddleY, int paddleWidth, int paddleHeight, boolean isLeftPaddle) {
        int halfBallSize = ballSize / 2;
        int ballLeft = ballX - halfBallSize;
        int ballRight = ballX + halfBallSize;
        int ballTop = ballY + halfBallSize;
        int ballBottom = ballY - halfBallSize;

        int paddleLeft = isLeftPaddle ? paddleX : paddleX - paddleWidth;
        int paddleRight = isLeftPaddle ? paddleX + paddleWidth : paddleX;
        int paddleTop = paddleY + paddleHeight / 2;
        int paddleBottom = paddleY - paddleHeight / 2;

        // Verifica se há sobreposição nos eixos x e y
        boolean overlapX = (ballLeft < paddleRight) && (ballRight > paddleLeft);
        boolean overlapY = (ballBottom < paddleTop) && (ballTop > paddleBottom);

        return overlapX && overlapY;
    }
}
