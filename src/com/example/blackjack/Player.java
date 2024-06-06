package com.example.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected List<Card> hand;
    private int score; // 總積分

    public Player() {
        hand = new ArrayList<>();
        score = 10;
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
