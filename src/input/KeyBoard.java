package input;

import cena.Cena;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyBoard implements KeyListener {
    private Cena cena;

    public KeyBoard(Cena cena) {
        this.cena = cena;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                cena.paddle1Y += 10;
                break;
            case KeyEvent.VK_DOWN:
                cena.paddle1Y -= 10;
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_P: 
                cena.togglePause();
                break;
            case KeyEvent.VK_S:
                System.exit(0);
                break;
            case KeyEvent.VK_B:
                cena.resetBall();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
