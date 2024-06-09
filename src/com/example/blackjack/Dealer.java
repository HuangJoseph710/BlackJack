package com.example.blackjack;

public class Dealer extends Player {
    public Dealer(int id) {
        super(id);
    }

    public boolean shouldHit() {
        // 規定莊家幣續持續抽牌 至少17點
        return calculateValue() < 17;
    }
}
