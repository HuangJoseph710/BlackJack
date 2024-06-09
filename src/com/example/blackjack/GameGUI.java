package com.example.blackjack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameGUI extends JFrame {
    private JPanel mainContainerPanel;
    private JPanel mainPanel;
    private JPanel scorePanel;
    private JTextArea logTextArea;
    private JButton hitButton;
    private JButton standButton;
    private List<PlayerPanel> playerPanels;
    private DealerPanel dealerPanel;
    private List<ScorePanel> scorePanels;
    private Dealer dealer;
    private List<Player> players;
    private Deck deck;
    public static int currentPlayerIndex;

    public GameGUI(int numPlayers) {
        // 設置主框架
        setTitle("Blackjack Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 初始化三大區塊
        mainContainerPanel = new JPanel(new BorderLayout());

        scorePanel = new JPanel(new GridLayout(numPlayers + 3, 1)); // 其實只需要(numPlayers+2)個rows，但為了牌版好看改成+3
        scorePanel.setBorder(new EmptyBorder(10, 15, 10, 20)); // 設置padding
        scorePanel.setOpaque(true); // 設置區塊透明

        // 初始化並添加LogTextArea到ScrollPane
        logTextArea = new JTextArea(10, 50);
        logTextArea.setBorder(new EmptyBorder(-10, 10, 10, 10)); 
        logTextArea.setEditable(false);
        logTextArea.setFont(new Font(logTextArea.getFont().getName(), Font.PLAIN, 14));
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        

        // 添加區塊到主框架
        add(mainContainerPanel, BorderLayout.CENTER);
        add(scorePanel, BorderLayout.EAST);
        add(logScrollPane, BorderLayout.SOUTH);

        // 初始化玩家和莊家
        dealer = new Dealer(numPlayers); // 莊家的id 設為最後一位玩家
        players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i)); // 第一位玩家id為0，也代表遊戲由 "id為0者" 開始
        }
        deck = new Deck();

        // 初始化並添加PlayerPanels和DealerPanel到mainPanel
        mainPanel = new JPanel(new GridLayout(1, (numPlayers + 1), 10, 5)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("src\\com\\example\\blackjack\\assets\\background.png"); // 替換成你實際的圖片路徑
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        dealerPanel = new DealerPanel(dealer);
        mainPanel.add(dealerPanel);

        playerPanels = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            PlayerPanel playerPanel = new PlayerPanel(players.get(i));
            playerPanels.add(playerPanel);
            mainPanel.add(playerPanel);
        }

        JLabel scoreTitle = new JLabel("玩家分數", SwingConstants.CENTER);
        scoreTitle.setFont(new Font(scoreTitle.getFont().getName(), Font.BOLD, 20));
        scoreTitle.setBorder(new EmptyBorder(-10, 20, -30, 20));
        scorePanel.add(scoreTitle);

        // 初始化並添加ScorePanels到scorePanel
        scorePanels = new ArrayList<>();
        ScorePanel dealerScorePanel = new ScorePanel("莊家", dealer.getScore());
        scorePanels.add(dealerScorePanel);
        scorePanel.add(dealerScorePanel);

        for (int i = 0; i < numPlayers; i++) {
            ScorePanel playerScorePanel = new ScorePanel("玩家 " + (i + 1), players.get(i).getScore());
            scorePanels.add(playerScorePanel);
            scorePanel.add(playerScorePanel);
        }


        // 創建buttonPanel並設置為GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // 增加按鈕之間的間距
        gbc.anchor = GridBagConstraints.CENTER; // 設置按鈕在面板中居中
        hitButton = new JButton("加牌");
        standButton = new JButton("停牌");

        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hit();
            }
        });

        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stand();
            }
        });

        buttonPanel.add(hitButton,gbc);
        gbc.gridx++;
        buttonPanel.add(standButton,gbc);

        // 將mainPanel和buttonPanel 在主框架(mainContainerPanel)裡上下排列
        mainContainerPanel.add(mainPanel, BorderLayout.CENTER);
        mainContainerPanel.add(buttonPanel, BorderLayout.SOUTH);

        

        pack();
        // setLocationRelativeTo(null); // 窗口居中
        setSize(1300, 750); // 設置窗口初始大小，寬度800，高度600
        setVisible(true);

        // 開始遊戲
        currentPlayerIndex = 0;
        startGame();
    }

    // 開始遊戲
    private void startGame() {
        log("\n開始一輪新遊戲!");
        dealInitialCards();
        updatePlayerTurn();
    }

    // 發初始牌
    private void dealInitialCards() {
        for (Player player : players) {
            player.receiveCard(deck.dealCard());
            player.receiveCard(deck.dealCard());
        }
        dealer.receiveCard(deck.dealCard());
        dealer.receiveCard(deck.dealCard());

        updatePlayerPanels();
    }

    // 更新所有玩家手牌面板
    private void updatePlayerPanels() {
        for (PlayerPanel playerPanel : playerPanels) {
            playerPanel.updatePanel();
        }
        dealerPanel.updatePanel();
    }

    // 更新得分板
    public void updateScorePanel() {
        scorePanels.get(0).updateScore(dealer.getScore());
        for (int i = 0; i < scorePanels.size()-1; i++) {
            scorePanels.get(i+1).updateScore(players.get(i).getScore());
        }
    }

    // 添加遊戲歷程訊息
    public void log(String message) {
        logTextArea.append(message + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
    }

    // 玩家選擇加牌
    private void hit() {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.receiveCard(deck.dealCard());
        log("玩家 " + (currentPlayerIndex + 1) + " 選擇加牌。");
        updatePlayerPanels();

        if (currentPlayer.calculateValue() > 21) {
            log("玩家 " + (currentPlayerIndex + 1) + " 爆牌!");
            nextPlayer();
        }
    }

    // 玩家選擇停牌
    private void stand() {
        log("玩家 " + (currentPlayerIndex + 1) + " 選擇停牌。");
        nextPlayer();
    }

    // 切換到下一個玩家
    private void nextPlayer() {
        JPanel currentCardPanel = playerPanels.get(currentPlayerIndex).getCardPanel();
        currentCardPanel.setOpaque(false); // 把目前玩家的cardPanel變成透明
        currentCardPanel.repaint(); // 強制重繪面板

        // 將cardPanel改成不透明


        
        currentPlayerIndex++;
        updatePlayerPanels();
        if (currentPlayerIndex < players.size()) {
            updatePlayerTurn();
        } else {
            dealerTurn();
        }
    }

    // 更新當前玩家的回合
    private void updatePlayerTurn() {
        log("\n輪到玩家 " + (currentPlayerIndex + 1) + " 的回合。");

        JPanel currentCardPanel = playerPanels.get(currentPlayerIndex).getCardPanel();
        currentCardPanel.setOpaque(true); // 把目前玩家的cardPanel變成透明
        currentCardPanel.repaint(); // 強制重繪面板 :用來解決渲染不完全的問題
    }

    // 莊家回合
    private void dealerTurn() {
        log("\n輪到莊家開牌。");
        updatePlayerPanels();
        while (dealer.shouldHit()) {
            log("莊家牌點未滿17，將再補一張牌。");
            dealer.receiveCard(deck.dealCard());
            updatePlayerPanels();
        }
        log("莊家回合結束。\n");
        determineWinners();
        promptNextRound();
    }

    // 判定勝負
    private void determineWinners() {
        int dealerHandValue = dealer.calculateValue();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int playerHandValue = player.calculateValue();

            if (playerHandValue > 21) {
                log("玩家 " + (i + 1) + " 爆牌! 莊家贏得這局!");
                dealer.addScore(2);
                player.minusScore(2);
            } else if (dealerHandValue > 21 || playerHandValue > dealerHandValue) {
                log("玩家 " + (i + 1) + " 贏得這局!");
                dealer.minusScore(2);
                player.addScore(2);
            } else if (playerHandValue < dealerHandValue) {
                log("玩家 " + (i + 1) + " 輸了這局!");
                dealer.addScore(2);
                player.minusScore(2);
            } else {
                log("莊家和玩家 " + (i + 1) + " 平手!");
                dealer.minusScore(1);
                player.addScore(1);
            }

            updateScorePanel();
        }
    }

    // 提示下一局遊戲
    private void promptNextRound() {
        int response = JOptionPane.showConfirmDialog(this, "是否要再玩一局?", "繼續遊戲", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            resetForNextRound();
        } else {
            endGame();
        }
    }

    // 重置手牌並開始新的一局
    private void resetForNextRound() {
        for (Player player : players) {
            player.resetHand();
        }
        dealer.resetHand();
        deck = new Deck(); // 重置牌堆
        currentPlayerIndex = 0;
        startGame();
    }

    // 結束遊戲，顯示最終得分
    private void endGame() {
        StringBuilder endMessage = new StringBuilder("-------  感謝你的遊玩! 最終分數:  -------\n");
        endMessage.append("莊家: ").append(dealer.getScore()).append("分\n");

        List<Integer> highestScorePlayer = new ArrayList<>();
        int maxScore = Integer.MIN_VALUE;
        for (int i = 0; i < players.size(); i++) {
            int playerScore = players.get(i).getScore();
            endMessage.append("玩家").append(i + 1).append(": ").append(playerScore).append("分\n");
            if (playerScore > maxScore) {
                maxScore = playerScore;
                highestScorePlayer.clear();
                highestScorePlayer.add(i);
            } else if (playerScore == maxScore) {
                highestScorePlayer.add(i);
            }
        }

        endMessage.append("恭喜!!");
        for (int i = 0; i < highestScorePlayer.size(); i++) {
            endMessage.append(" 玩家").append(highestScorePlayer.get(i) + 1);
            if (i < highestScorePlayer.size() - 1) {
                endMessage.append(",");
            }
        }
        endMessage.append(" 獲得最高分: ").append(players.get(highestScorePlayer.get(0)).getScore());

        JOptionPane.showMessageDialog(this, endMessage.toString());
        System.exit(0);
    }

    public static void main(String[] args) {
        // 使用SwingUtilities.invokeLater來確保GUI操作在EDT上進行
        SwingUtilities.invokeLater(() -> {
            int numberOfPlayers = 1;

            // 提示用戶輸入玩家數量
            while (true) {
                String input = JOptionPane.showInputDialog("請輸入玩家數量 (1~4):");

                // 檢查用戶是否點擊了取消或關閉了對話框
                if (input == null) {
                    JOptionPane.showMessageDialog(null, "程式已取消。");
                    return; // 終止程式執行
                }

                try {
                    numberOfPlayers = Integer.parseInt(input);
                    if (numberOfPlayers >= 1 && numberOfPlayers <= 4) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "玩家數量必須在1~4之間。");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "請輸入有效的數字。");
                }
            }

            // 創建並顯示BlackjackGameGUI
            new GameGUI(numberOfPlayers);
        });
    }
}