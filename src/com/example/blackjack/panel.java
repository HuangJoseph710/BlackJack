package com.example.blackjack;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.util.List;

// 堆疊卡牌Panel
class CardPanel extends JPanel {
    private List<Card> hand;
    private int PlayerIndex;

    public CardPanel(List<Card> list, int PlayerIndex) {
        this.hand = list;
        this.PlayerIndex = PlayerIndex;
        setBackground(new Color(0xFFD166)); // 設定背景顏色
        setOpaque(false); //將背景改為透明的，避免遮擋playerPanel的背景顏色
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 將 Graphics 轉換為 Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        // 啟用抗鋸齒和高質量渲染
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = 10;
        int y = 10;
        double scale = 0.9;
        
            for (Card card : hand) {
                ImageIcon cardIcon = new ImageIcon("src/com/example/blackjack/assets/"+card+".png");

                // 還沒輪到的玩家 第二張牌會蓋著
                if (PlayerIndex > GameGUI.currentPlayerIndex && hand.indexOf(card) > 0) {
                    cardIcon = new ImageIcon("src/com/example/blackjack/assets/card_back.png");
                }

                // 使用原始大小的圖片繪製
                Image image = cardIcon.getImage();
                int imageWidth = (int)(image.getWidth(null) * scale);
                int imageHeight = (int)(image.getHeight(null) * scale);
                // g.drawImage(cardIcon.getImage(), x, y, this);
                g.drawImage(image, x, y, imageWidth, imageHeight, null);
                x = (int)(x + 17 * scale); // 向右偏移
                y = (int)(y + 45 * scale); // 向下偏移
            }
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
        repaint();
    }
}

// 玩家Panel
class PlayerPanel extends JPanel {
    private CardPanel handPanel;
    private JLabel valueLabel;
    private Player player;

    public PlayerPanel(Player player) {
        this.player = player;
        setOpaque(false);

        setLayout(new BorderLayout());
        valueLabel = new JLabel(player.calculateValue()+"", SwingConstants.CENTER);
        valueLabel.setPreferredSize(new Dimension(30, 25));
        valueLabel.setFont(new Font(valueLabel.getFont().getName(), Font.BOLD, 18));
        valueLabel.setBackground(new Color(17, 138, 178, 255));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setOpaque(true);
        handPanel = new CardPanel(player.getHand(), player.getId());

        add(valueLabel, BorderLayout.NORTH);
        add(handPanel, BorderLayout.CENTER);

        // 設置title邊框
        TitledBorder titledBorder = new TitledBorder(new LineBorder(new Color(0x118ab2), 1), ("玩家 " + (player.getId()+1)));
        titledBorder.setTitleFont(new Font(titledBorder.getTitleFont().getName(), Font.BOLD, 18));
        titledBorder.setTitleColor(Color.WHITE); 

        // 使用 CompoundBorder 將 空邊框(padding) 和 TitledBorder 結合起來
        CompoundBorder compoundBorder = new CompoundBorder(new EmptyBorder(10, 0, 40, 15), titledBorder);
        compoundBorder = new CompoundBorder(compoundBorder, new EmptyBorder(10, 0, 0, 0));
        // 設置邊框
        setBorder(compoundBorder);
    }

    public Player getPlayer() {
        return player;
    }

    public CardPanel getCardPanel(){
        return handPanel;
    }

    public void updatePanel() {
        valueLabel.setText(player.calculateValue() +""); // 計算牌點
        // 還沒輪到的玩家，只顯示第一張牌的牌點
        if (player.getId() > GameGUI.currentPlayerIndex){
            valueLabel.setText(player.getHand().get(0).getValue()+"");
        }
        handPanel.setHand(player.getHand());
    }
}

class DealerPanel extends JPanel {
    private CardPanel handPanel;
    private JLabel valueLabel;
    private Dealer dealer;

    public DealerPanel(Dealer dealer) {
        this.dealer = dealer;

        setBackground(new Color(0xFFD166)); // 設定背景顏色
        setOpaque(false);

        setLayout(new BorderLayout());
        valueLabel = new JLabel(dealer.calculateValue() + "", SwingConstants.CENTER);
        valueLabel.setPreferredSize(new Dimension(30, 25));
        valueLabel.setFont(new Font(valueLabel.getFont().getName(), Font.BOLD, 18));
        valueLabel.setBackground(new Color(17, 138, 178, 255));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setOpaque(true);
        handPanel = new CardPanel(dealer.getHand(), dealer.getId());

        add(valueLabel, BorderLayout.NORTH);
        add(handPanel, BorderLayout.CENTER);

        TitledBorder titledBorder = new TitledBorder(new LineBorder(new Color(0x118ab2), 1), ("莊家 "));
        titledBorder.setTitleFont(new Font(titledBorder.getTitleFont().getName(), Font.BOLD, 18));
        titledBorder.setTitleColor(Color.WHITE); 

        // 使用 CompoundBorder 將 空邊框(padding) 和 TitledBorder 結合起來
        CompoundBorder compoundBorder = new CompoundBorder(new EmptyBorder(10, 0, 40, 15), titledBorder);
        compoundBorder = new CompoundBorder(compoundBorder, new EmptyBorder(10, 0, 0, 0));
        // 設置邊框
        setBorder(compoundBorder);
    }

    public void updatePanel() {
        valueLabel.setText(dealer.calculateValue() +"");
        // 還沒輪到的玩家，只顯示第一張牌的牌點
        if (dealer.getId() > GameGUI.currentPlayerIndex){
            valueLabel.setText(dealer.getHand().get(0).getValue()+"");
        }
        handPanel.setHand(dealer.getHand());
    }
}

class ScorePanel extends JPanel {
    private JLabel scoreLabel;
    private JLabel nameLabel;
    private String name;

    public ScorePanel(String name, int score) {
        this.name = name;
        setLayout(new GridLayout(3, 1));
        add(new Label());

        nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.BOLD, 18));
        add(nameLabel, BorderLayout.NORTH);

        scoreLabel = new JLabel(score + "分", SwingConstants.CENTER);
        scoreLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.PLAIN, 17));
        add(scoreLabel, BorderLayout.CENTER);

    }

    public void updateScore(int score) {
        nameLabel.setText(name);
        scoreLabel.setText(score + "分");
    }
}
