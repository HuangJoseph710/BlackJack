package com.example.blackjack;

import javax.swing.*;
import java.awt.*;

class PlayerPanel extends JPanel {
    private JLabel handLabel;
    private JLabel valueLabel;
    private Player player;

    public PlayerPanel(Player player) {
        this.player = player;
        setLayout(new BorderLayout());
        valueLabel = new JLabel("點數:" + player.calculateValue(), SwingConstants.CENTER);
        valueLabel.setFont(new Font(valueLabel.getFont().getName(), Font.PLAIN, 18)); // 設置字體大小為 18 ；字體保持不變/正常字體
        handLabel = new JLabel("手牌: " + player.getHand().toString());
        add(handLabel, BorderLayout.CENTER);
        add(valueLabel, BorderLayout.NORTH);
        // setBorder(BorderFactory.createTitledBorder("玩家"));
        setBorder(BorderFactory.createTitledBorder("玩家 " + (player.getId())));
    }

    public Player getPlayer() {
        return player;
    }

    public void updatePanel() {
        valueLabel.setText("點數:" + player.calculateValue());
        handLabel.setText("手牌: " + player.getHand().toString());
    }
}

class DealerPanel extends JPanel {
    private JLabel handLabel;
    private JLabel valueLabel;
    private Dealer dealer;

    public DealerPanel(Dealer dealer) {
        this.dealer = dealer;
        setLayout(new BorderLayout());
        valueLabel = new JLabel("點數: " + dealer.calculateValue(), SwingConstants.CENTER);
        valueLabel.setFont(new Font(valueLabel.getFont().getName(), Font.PLAIN, 18));
        handLabel = new JLabel("手牌: " + dealer.getHand().toString());
        add(handLabel, BorderLayout.CENTER);
        add(valueLabel, BorderLayout.NORTH);
        setBorder(BorderFactory.createTitledBorder("莊家"));
    }

    public void updatePanel() {
        valueLabel.setText("點數:" + dealer.calculateValue());
        handLabel.setText("手牌: " + dealer.getHand().toString());
    }
}

class ScorePanel extends JPanel {
    private JLabel scoreLabel;
    private String name;

    public ScorePanel(String name, int score) {
        this.name = name;
        setLayout(new BorderLayout());
        scoreLabel = new JLabel(name + " 分數: " + score);
        add(scoreLabel, BorderLayout.CENTER);
    }

    public void updateScore(int score) {
        scoreLabel.setText(name + " 分數: " + score);
    }
}
