package com.mycompany.projetofinal;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Random;

/**
 * TelaInicial class for the tic-tac-toe game.
 * Author: rafar
 */
public class TelaInicial {
    JFrame telaInicial = new JFrame("Tela inicial");
    private int jogadorDaVez = 1;
    private int[][] posTabuleiro = new int[3][3];
    private JButton botaoSingleplayer, botaoMultiplayer, botaoPontuacoes;
    private JButton botaoVoltarUm, botaoVoltarDois, botaoVoltarTres;
    private JPanel cardPanel, panelButtons;
    private JButton[] buttonsSingleplayer = new JButton[9];
    private JButton[] buttonsMultiplayer = new JButton[9];
    private Random random = new Random();
    private boolean isSingleplayer = false;

    private int pontosJogador1 = 0;
    private int pontosJogador2 = 0;
    private int pontosMaquina = 0;

    private JLabel labelPontuacoes;

    public TelaInicial() {
        cardPanel = new JPanel(new CardLayout());

        botaoSingleplayer = new JButton("Singleplayer");
        botaoMultiplayer = new JButton("Multiplayer");
        botaoPontuacoes = new JButton("Pontuações");
        botaoVoltarUm = new JButton("Voltar");
        botaoVoltarDois = new JButton("Voltar");
        botaoVoltarTres = new JButton("Voltar");

        panelButtons = new JPanel(new GridLayout(3, 1, 10, 10));
        panelButtons.add(botaoSingleplayer);
        panelButtons.add(botaoMultiplayer);
        panelButtons.add(botaoPontuacoes);

        JPanel panel1 = createMainMenuPanel();
        JPanel panelSingleplayer = createGamePanel(botaoVoltarUm, buttonsSingleplayer);
        JPanel panelMultiplayer = createGamePanel(botaoVoltarDois, buttonsMultiplayer);
        JPanel panelPontuacoes = createPontuacoesPanel();

        cardPanel.add(panelButtons, "card1");
        cardPanel.add(panelSingleplayer, "card2");
        cardPanel.add(panelMultiplayer, "card3");
        cardPanel.add(panelPontuacoes, "card4");

        telaInicial.add(cardPanel);
        telaInicial.setSize(700, 450);
        telaInicial.setResizable(false);
        telaInicial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaInicial.setVisible(true);
        telaInicial.setLocationRelativeTo(null);

        initializeButtonActions();
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Menu do Jogo da Velha"));
        return panel;
    }

    private JPanel createGamePanel(JButton botaoVoltar, JButton[] buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.RED);
        JLabel label = new JLabel("MODO DE JOGO");
        label.setBounds(30, 10, 200, 40);
        panel.add(label);

        botaoVoltar.setBounds(195, 360, 300, 40);
        panel.add(botaoVoltar);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setBounds(210 + (i % 3) * 90, 50 + (i / 3) * 90, 90, 90);
            buttons[i].addActionListener(this::buttonAction);
            panel.add(buttons[i]);
        }

        return panel;
    }

    private JPanel createPontuacoesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.CYAN);
        JLabel label = new JLabel("PONTUAÇÕES");
        label.setBounds(30, 10, 200, 40);
        panel.add(label);

        labelPontuacoes = new JLabel();
        labelPontuacoes.setBounds(30, 60, 300, 60);
        panel.add(labelPontuacoes);

        botaoVoltarTres.setBounds(200, 360, 300, 40);
        panel.add(botaoVoltarTres);

        return panel;
    }

    private void initializeButtonActions() {
        botaoSingleplayer.addActionListener(e -> switchToCard("card2", buttonsSingleplayer, true));
        botaoMultiplayer.addActionListener(e -> switchToCard("card3", buttonsMultiplayer, false));
        botaoPontuacoes.addActionListener(e -> switchToCard("card4"));

        botaoVoltarUm.addActionListener(e -> voltarParaMenu());
        botaoVoltarDois.addActionListener(e -> voltarParaMenu());
        botaoVoltarTres.addActionListener(e -> switchToCard("card1"));
    }

    private void switchToCard(String cardName, JButton[] buttons, boolean singleplayer) {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, cardName);
        resetBoard(buttons);
        resetPosTabuleiro();
        isSingleplayer = singleplayer;
    }

    private void switchToCard(String cardName) {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, cardName);
        updatePontuacoesLabel();
    }

    private void voltarParaMenu() {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "card1");
        updatePontuacoesLabel();
    }

    private void resetBoard(JButton[] buttons) {
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
        }
    }

    private void resetPosTabuleiro() {
        jogadorDaVez = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                posTabuleiro[i][j] = 0;
            }
        }
    }

    private void buttonAction(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        int index = -1;
        for (int i = 0; i < 9; i++) {
            if (buttonsSingleplayer[i] == button || buttonsMultiplayer[i] == button) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            int row = index / 3;
            int col = index % 3;

            if (posTabuleiro[row][col] == 0) {
                if (jogadorDaVez == 1) {
                    button.setText("X");
                    posTabuleiro[row][col] = 1;
                    jogadorDaVez = 2;
                    button.setEnabled(false);
                    if (checkWinner()) {
                        JOptionPane.showMessageDialog(telaInicial, "Jogador 1 venceu!");
                        pontosJogador1++;
                        resetGame();
                    } else if (isBoardFull()) {
                        JOptionPane.showMessageDialog(telaInicial, "Empate!");
                        resetGame();
                    } else if (isSingleplayer) {
                        machineMove();
                    }
                } else if (jogadorDaVez == 2 && !isSingleplayer) {
                    button.setText("O");
                    posTabuleiro[row][col] = 2;
                    jogadorDaVez = 1;
                    button.setEnabled(false);
                    if (checkWinner()) {
                        JOptionPane.showMessageDialog(telaInicial, "Jogador 2 venceu!");
                        pontosJogador2++;
                        resetGame();
                    } else if (isBoardFull()) {
                        JOptionPane.showMessageDialog(telaInicial, "Empate!");
                        resetGame();
                    }
                }
            }
        }
    }

    private void machineMove() {
        int row, col;
        do {
            int index = random.nextInt(9);
            row = index / 3;
            col = index % 3;
        } while (posTabuleiro[row][col] != 0);

        posTabuleiro[row][col] = 2;
        buttonsSingleplayer[row * 3 + col].setText("O");
        buttonsSingleplayer[row * 3 + col].setEnabled(false);
        jogadorDaVez = 1;

        if (checkWinner()) {
            JOptionPane.showMessageDialog(telaInicial, "Máquina venceu!");
            pontosMaquina++;
            resetGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(telaInicial, "Empate!");
            resetGame();
        }
    }

    private void resetGame() {
        resetBoard(buttonsSingleplayer);
        resetBoard(buttonsMultiplayer);
        resetPosTabuleiro();
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (posTabuleiro[i][0] == posTabuleiro[i][1] && posTabuleiro[i][1] == posTabuleiro[i][2] && posTabuleiro[i][0] != 0) {
                return true;
            }
            if (posTabuleiro[0][i] == posTabuleiro[1][i] && posTabuleiro[1][i] == posTabuleiro[2][i] && posTabuleiro[0][i] != 0) {
                return true;
            }
        }
        if (posTabuleiro[0][0] == posTabuleiro[1][1] && posTabuleiro[1][1] == posTabuleiro[2][2] && posTabuleiro[0][0] != 0) {
            return true;
        }
        if (posTabuleiro[0][2] == posTabuleiro[1][1] && posTabuleiro[1][1] == posTabuleiro[2][0] && posTabuleiro[0][2] != 0) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (posTabuleiro[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updatePontuacoesLabel() {
        String pontuacoesText = String.format("<html>Jogador 1: %d<br>Jogador 2: %d<br>Máquina: %d</html>", pontosJogador1, pontosJogador2, pontosMaquina);
        labelPontuacoes.setText(pontuacoesText);
    }
}
