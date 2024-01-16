package com.example.task_2.Program;

import java.util.List;

public class Combination implements Comparable<Combination> {

    public enum Type {
        ROYAL_FLUSH,
        STRAIGHT_FLUSH,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        FLUSH,
        STRAIGHT,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    private final static double[] coefficients = {1, 0.999985, 0.999976, 0.998559, 0.998033, 0.996075, 0.978871, 0.952461, 0.577431, 0.498823, 0.4};
    private final double strength;
    private final List<Card> cards;
    private final Type type;

    private Combination(List<Card> cards, Type type) {
        this.cards = cards;
        this.type = type;
        strength = calcStrength();
    }

    public static Combination defineCombination(List<Card> cards) {
        Type type = null;
        if (cards.size() >= 5) {
            //...
        } else if (cards.size() >= 4) {
            //...
        } else if (cards.size() >= 3) {
            //...
        } else if (cards.size() >= 2) {
            //...
        }
        return new Combination(cards, Type.HIGH_CARD);
    }

    private int maxDignity() {
        int dignity = 0;
        for (Card card : cards) {
            if (card.dignity() > dignity) {
                dignity = card.dignity();
            }
        }
        return dignity;
    }

    private double calcStrength() {
        if (type.equals(Type.ROYAL_FLUSH)) {
            return coefficients[0];
        } else {
            int dignity = maxDignity() - 1;
            int index = 0;
            for (Type t : Type.values()) {
                if (t.equals(type)) {
                    break;
                }
                index++;
            }
            return coefficients[index + 1] + dignity * (coefficients[index] - coefficients[index + 1]) / 13;
        }
    }

    public double getStrength() {
        return strength;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Type getType() {
        return type;
    }

    @Override
    public int compareTo(Combination o) {
        return 0;
    }
}
