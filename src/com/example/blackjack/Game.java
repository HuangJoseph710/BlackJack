package com.example.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Deck deck;
    private List<Player> players;
    private Dealer dealer;

    public Game() {
        deck = new Deck();
        players = new ArrayList<>();
        dealer = new Dealer(0);
    }

    public void startGame() {
    	Scanner scanner = new Scanner(System.in);
    	int numPlayers = 0;
    	while (true) {
            System.out.print("輸入玩家人數(1-4): ");
            if (scanner.hasNextInt()) {
                numPlayers = scanner.nextInt();
                scanner.nextLine(); // 清除緩衝區中的換行符
                if (numPlayers >= 1 && numPlayers <= 4) {
                    break;
                } else {
                    System.out.println("==========ERROR==========\n　 請輸入1到4之間的數字！\n");
                }
            } else {
                System.out.println("==========ERROR==========\n　 請輸入有效的數字！\n");
                scanner.nextLine(); // 清除緩衝區中的無效輸入
            }
        }
    	
    	for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i+1));
        }
    	
        boolean keepPlaying = true;
        
        while (keepPlaying) {
            playRound(scanner);
            System.out.println("莊家目前分數: " + dealer.getScore());
            System.out.print("是否要再玩一局? (y/n): ");
            String response = scanner.nextLine();
            keepPlaying = response.equalsIgnoreCase("y");

            // 為新的一局 重置手牌
            for (Player player : players) {
                player.resetHand();
            }
            dealer.resetHand();
            deck = new Deck(); // 重置牌堆
        }

        System.out.println("-------  感謝你的遊玩! 最終分數:  -------");
        System.out.println("莊家:\t" + dealer.getScore());
        
        List<Integer> highestScorePlayer = new ArrayList<>();
        int maxScore = Integer.MIN_VALUE;
        for (int i = 0; i < players.size(); i++) {
        	int playerScore = players.get(i).getScore();
            System.out.println("玩家" + (i + 1) + ":\t" + playerScore);
            if (playerScore > maxScore) {
                maxScore = playerScore;
                highestScorePlayer.clear(); // 清空之前的索引
                highestScorePlayer.add(i); // 添加新的索引
            } else if (playerScore == maxScore) {
            	highestScorePlayer.add(i); // 添加相同得分的索引
            }
        }
        
        System.out.print("恭喜!!");
        for (int i = 0; i < highestScorePlayer.size(); i++) {
            System.out.print(" 玩家" + (highestScorePlayer.get(i) + 1));
            if (i < highestScorePlayer.size() - 1) {
                System.out.print(","); // 添加分隔符號
            }
        }
        System.out.println(" 獲得最高分: " + players.get(highestScorePlayer.get(0)).getScore());
        scanner.close();
    }
    
    public void playRound(Scanner scanner) {
    	
    	// 初始化 發牌
    	for (Player player : players) {
            player.receiveCard(deck.dealCard());
            player.receiveCard(deck.dealCard());
        }
        dealer.receiveCard(deck.dealCard());
        dealer.receiveCard(deck.dealCard());

        // 玩家回合
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
	        while (true) {
	            System.out.println("\n玩家" + (i+1) + "手牌: " + player.getHand() + " | 點數和: " + player.calculateValue());
	            System.out.print("是否要加牌(hit) 還是stand? (h/s): ");
	            String action = scanner.nextLine();
	            if (action.equalsIgnoreCase("h")) {
	            	System.out.println("玩家"+(i+1)+"選擇 加牌hit");
	                player.receiveCard(deck.dealCard());
	                if (player.calculateValue() > 21) {
	                	System.out.println("\n玩家" + (i+1) + "手牌: " + player.getHand() + " | 點數和: " + player.calculateValue());
	                    System.out.println("玩家" + (i+1) + "爆牌!");
	                    break;
	                }
	            } else {
	            	System.out.println("玩家"+(i+1)+"選擇 停牌stand");
	                break;
	            }
	        }
        }

        // 莊家回合
        while (dealer.shouldHit()) {
            dealer.receiveCard(deck.dealCard());
        }

        System.out.println("\n莊家手牌: " + dealer.getHand() + " | 點數和: " + dealer.calculateValue());

        // 判斷贏家
        int dealerHandValue = dealer.calculateValue();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int playerHandValue = player.calculateValue();
            
            if (playerHandValue > 21) {
                System.out.println("玩家" + (i + 1) + " 爆牌! 莊家贏得這局!");
                dealer.addScore(2);
                player.minusScore(2);
            } else if (dealerHandValue > 21 || playerHandValue > dealerHandValue) {
                System.out.print("玩家" + (i + 1) + " 贏得這局!");
                dealer.minusScore(2);
                player.addScore(2);
            } else if (playerHandValue < dealerHandValue) {
                System.out.print("玩家" + (i + 1) + "輸了這局!");
                dealer.addScore(2);
                player.minusScore(2);
            } else {
                System.out.print("莊家和玩家" + (i + 1) + "平手!");
                dealer.minusScore(1);
                player.addScore(1);
            }

            System.out.println("\t目前分數: " + player.getScore());
            
        }
    }

}
