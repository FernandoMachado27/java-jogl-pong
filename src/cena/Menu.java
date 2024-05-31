package cena;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Menu {

    private static int userChoice = JOptionPane.CLOSED_OPTION;

    public static int menu() {
        JFrame frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Abrir em tela cheia

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel, BorderLayout.CENTER);
        placeComponents(panel);

        frame.setVisible(true);

        // Esperar pela interação do usuário
        synchronized (frame) {
            try {
                frame.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return userChoice; // Retorna a escolha do usuário
    }

    private static void placeComponents(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Ajustar o espaçamento
        gbc.fill = GridBagConstraints.NONE; // Permitir expansão vertical e horizontal
        gbc.anchor = GridBagConstraints.CENTER;

        // Adiciona espaço vertical no topo
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        JLabel welcomeLabel = new JLabel("Seja Bem-Vindo ao Pong!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        JTextPane infoArea = new JTextPane();
        infoArea.setEditable(false);
        infoArea.setOpaque(false);
        infoArea.setFont(new Font("Serif", Font.PLAIN, 18));
        infoArea.setBackground(panel.getBackground());
        infoArea.setMargin(new Insets(20, 40, 20, 40)); // Adicionar margem
        infoArea.setAlignmentX(JTextPane.CENTER_ALIGNMENT);

        // Configurar o estilo do texto
        StyledDocument doc = infoArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        try {
            doc.insertString(0,
                "Nesta divertida aventura, o objetivo é rebater a bola e marcar pontos encima do adversário. Lembrando que o usuário inicia com 5 vidas e caso nao consiga rebater a bola, uma vida será perdida.\r\n" +
                "\r\n" +
                "Ao atingir 200 pontos, você passará para o nível 2, onde encontrará desafios que deixará o game ainda mais emocionante!\r\n" +
                "\r\n" +
                "Pressione P para pausar ou reiniciar. \r\n" +
                "\r\n" +
                "Pressione S caso precise sair da partida. \r\n" +
                "\r\n" +
                "Boa sorte!",
                center
            );
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.ipadx = 200; // Definir uma largura fixa para o JTextArea
        panel.add(infoArea, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.5; // Permitir expansão horizontal
        gbc.anchor = GridBagConstraints.EAST; // Alinhar à direita
        JButton yesButton = new JButton("Iniciar");
        yesButton.setFont(new Font("Serif", Font.PLAIN, 18)); // Reduzir o tamanho da fonte dos botões
        yesButton.addActionListener(e -> {
            userChoice = JOptionPane.YES_OPTION;
            synchronized (panel.getTopLevelAncestor()) {
                panel.getTopLevelAncestor().notify();
                ((JFrame) panel.getTopLevelAncestor()).dispose();
            }
        });
        panel.add(yesButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Alinhar à esquerda
        JButton noButton = new JButton("Fechar");
        noButton.setFont(new Font("Serif", Font.PLAIN, 18)); // Reduzir o tamanho da fonte dos botões
        noButton.addActionListener(e -> {
            userChoice = JOptionPane.NO_OPTION;
            synchronized (panel.getTopLevelAncestor()) {
                panel.getTopLevelAncestor().notify();
                ((JFrame) panel.getTopLevelAncestor()).dispose();
            }
        });
        panel.add(noButton, gbc);

        // Adiciona espaço vertical na parte inferior
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        panel.add(Box.createVerticalGlue(), gbc);
    }

    public static int menuPhaseTwo() {
        JFrame frame = new JFrame("Menu Fase 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Abrir em tela cheia

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel, BorderLayout.CENTER);
        placeComponentsPhaseTwo(panel);

        frame.setVisible(true);

        // Esperar pela interação do usuário
        synchronized (frame) {
            try {
                frame.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return userChoice; // Retorna a escolha do usuário
    }

    private static void placeComponentsPhaseTwo(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Ajustar o espaçamento
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel phaseTwoLabel = new JLabel("Parabéns, você passou para a fase 2!!", SwingConstants.CENTER);
        phaseTwoLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(phaseTwoLabel, gbc);

        JTextArea infoArea = new JTextArea("Deseja continuar? O progresso não será salvo.");
        infoArea.setWrapStyleWord(true);
        infoArea.setLineWrap(true);
        infoArea.setOpaque(false);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Serif", Font.PLAIN, 18));
        infoArea.setBackground(panel.getBackground());
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(infoArea, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.insets = new Insets(10, 10, 10, 10);
        gbcButton.gridx = 0;
        gbcButton.gridy = 0;
        gbcButton.fill = GridBagConstraints.NONE;

        JButton yesButton = new JButton("Continuar");
        yesButton.setFont(new Font("Serif", Font.PLAIN, 18));
        yesButton.addActionListener(e -> {
            userChoice = JOptionPane.YES_OPTION;
            synchronized (panel.getTopLevelAncestor()) {
                panel.getTopLevelAncestor().notify();
                ((JFrame) panel.getTopLevelAncestor()).dispose();
            }
        });
        buttonPanel.add(yesButton, gbcButton);

        gbcButton.gridx = 1;
        JButton noButton = new JButton("Sair");
        noButton.setFont(new Font("Serif", Font.PLAIN, 18));
        noButton.addActionListener(e -> {
            userChoice = JOptionPane.NO_OPTION;
            synchronized (panel.getTopLevelAncestor()) {
                panel.getTopLevelAncestor().notify();
                ((JFrame) panel.getTopLevelAncestor()).dispose();
            }
        });
        buttonPanel.add(noButton, gbcButton);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);
    }

    public static void menuLose() {
        JFrame frame = new JFrame("Fim de Jogo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Abrir em tela cheia

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel, BorderLayout.CENTER);
        placeComponentsLose(panel);

        frame.setVisible(true);

        // Esperar pela interação do usuário
        synchronized (frame) {
            try {
                frame.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void placeComponentsLose(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Ajustar o espaçamento
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel loseLabel = new JLabel("Você perdeu :(", SwingConstants.CENTER);
        loseLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridwidth = 2;
        panel.add(loseLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Serif", Font.PLAIN, 18));
        okButton.addActionListener(e -> {
            synchronized (panel.getTopLevelAncestor()) {
                panel.getTopLevelAncestor().notify();
                ((JFrame) panel.getTopLevelAncestor()).dispose();
            }
        });
        panel.add(okButton, gbc);
    }
}