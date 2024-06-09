package com.example.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected List<Card> hand;
    private int score; // 總積分
    private int id; // ID 除了"識別用途"以外，也代表遊玩的順序

    public Player(int id) {
        hand = new ArrayList<>();
        score = 10;
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public int calculateValue() {
        int value = 0;
        int aceCount = 0;

        for (Card card : hand) {
            value += card.getValue();
            if (card.getRank() == Card.Rank.ACE) {
                aceCount++;
            }
        }

        // 調整 Ace
        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }

        return value;
    }
    
    public void resetHand() {
        hand.clear();
    }

    public void addScore(int points) {
        score += points;
    }

    public void minusScore(int points) {
        score -= points;
    }

    public int getScore() {
        return score;
    }
    
    public List<Card> getHand() {
        return hand;
    }
}
