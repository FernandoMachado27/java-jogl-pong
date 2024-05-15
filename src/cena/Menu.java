package cena;

import javax.swing.JOptionPane;

public class Menu {
	
	public static int menu() {
	    JOptionPane.showMessageDialog(null, "Seja Bem-Vindo ao Pong!", "Aviso", JOptionPane.WARNING_MESSAGE);
	    JOptionPane.showMessageDialog(null, "O jogo começa com 5 vidas e placar 0. Cada vez que o usuário não conseguir rebater a bola, uma vida será perdida. \n "
	            + "A cada rebatida na bola a pontuação será incrementada até atingir 200 pontos. \n"
	            + "Se o usuário atingir 200 pontos a 2ª fase será iniciada. \n"
	            + "Para pausar e reiniciar o jogo, selecione a tecla P. \n"
	            + "para finalizar o jogo, selecione a tecla S. \n"
	            + "Bom jogo!", "Aviso",
	            JOptionPane.INFORMATION_MESSAGE);
	    
	    return JOptionPane.showConfirmDialog(null, "Você deseja continuar?", "Confirmação", JOptionPane.YES_NO_OPTION);
	}
	
	public static int menuPhaseTwo() {
		return JOptionPane.showConfirmDialog(null, "Parabéns, você passou para a fase 2!! \n"
				+ "Deseja continuar? O progresso não será salvo.", "Confirmação", JOptionPane.YES_NO_OPTION);
	}
	
	public static void menuLose() {
		JOptionPane.showMessageDialog(null, "Você perdeu :( ", "Aviso", JOptionPane.WARNING_MESSAGE);
	}

}
	