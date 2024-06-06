package com.example.blackjack;

public class Dealer extends Player {
    public boolean shouldHit() {
        return calculateValue() < 17;
    }
}
