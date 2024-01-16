package com.example.task_2.Program;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;

public class Card {

    public static class SuitComparator implements Comparator<Card> {

        @Override
        public int compare(Card o1, Card o2) {
            int value1 = getSuitValue(o1.suit());
            int value2 = getSuitValue(o2.suit());
            return Integer.compare(value1, value2);
        }

        private int getSuitValue(Suit suit) {
            if (suit.equals(Suit.HEARTS)) {
                return 4;
            }
            if (suit.equals(Suit.DIAMONDS)) {
                return 3;
            }
            if (suit.equals(Suit.CLUBS)) {
                return 2;
            }
            return 1;
        }
    }

    public static class DignityComparator implements Comparator<Card> {

        @Override
        public int compare(Card o1, Card o2) {
            int value1 = o1.dignity();
            int value2 = o2.dignity();
            return Integer.compare(value1, value2);
        }
    }

    public enum Suit {
        HEARTS,
        DIAMONDS,
        CLUBS,
        SPADES
    }

    private final Suit suit;
    private final int dignity;
    private final Image picture;

    public Card(Suit suit, int dignity, Image face) {
        this.picture = face;
        this.suit = suit;
        this.dignity = dignity;
    }

    public Suit suit() {
        return suit;
    }

    public int dignity() {
        return dignity;
    }

    public static Image reverse() {
        try {
            return new Image(new FileInputStream(Settings.getSettings().getCardsDirectory() + "\\reverse.png"));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public Image picture() {
        return picture;
    }


    public static SuitComparator suitComparator() {
        return new SuitComparator();
    }

    public static DignityComparator diginityComparator() {
        return new DignityComparator();
    }
}
