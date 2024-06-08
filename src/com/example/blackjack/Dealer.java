package com.example.blackjack;

public class Dealer extends Player {
    public Dealer(int id) {
        super(id);
    }

    public boolean shouldHit() {
        return calculateValue() < 17;
    }
}
