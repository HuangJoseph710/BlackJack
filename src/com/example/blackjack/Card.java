package com.example.blackjack;

public class Card {
    public enum Suit {
//        HEARTS, DIAMONDS, CLUBS, SPADES
    	H, D, C, S
    }

//    public enum Rank {
//        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
//    }
    public enum Rank {
        ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13);

        private final int num;

        private Rank(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        switch (rank) {
            case TWO: return 2;
            case THREE: return 3;
            case FOUR: return 4;
            case FIVE: return 5;
            case SIX: return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE: return 9;
            case TEN: case JACK: case QUEEN: case KING: return 10;
            case ACE: return 11; // Aces can also be 1, handled elsewhere
            default: throw new IllegalArgumentException("Unknown rank: " + rank);
        }
    }

    @Override
    public String toString() {
        return suit + "/" + rank.getNum();
    }
}
