package com.example.task_2.Program;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private final String name;
    private final Image portrait;
    private int decision;
    private int money;
    private Card card1;
    private Card card2;
    private List<Combination> expectedCombinations;
    private Combination currentCombination;

    public Player(String name, int money, Image portrait) {
        this.portrait = portrait;
        this.name = name;
        this.money = money;
    }

    public void setCard1(Card card1) {
        this.card1 = card1;
    }

    public void setCard2(Card card2) {
        this.card2 = card2;
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
        return card2;
    }

    public void defineCurrentCombination() {
        Game game = Game.getGame();
        List<Card> hand = new ArrayList<>();
        hand.add(card1);
        hand.add(card2);
        if (game.getPhase() >= 4) {
            hand.add(game.getTableCards().get(0));
            hand.add(game.getTableCards().get(1));
            hand.add(game.getTableCards().get(2));
        }
        if (game.getPhase() >= 5) {
            hand.add(game.getTableCards().get(3));
        }
        if (game.getPhase() == 6) {
            hand.add(game.getTableCards().get(4));
        }
        currentCombination = Combination.defineCombination(hand);
    }

    public void defineExpectedCombinations() {
        // . . .
    }

    public void resetDecision() {
        decision = -2;
    }

    public void fold() {
        dropCards();
        decision = -1;
        Game.getGame().decrementActivePlayersCount();
    }

    public void check() {
        decision = 0;
        Game.getGame().setLastDecisionCode(0);
    }

    public void raise() {
        Random rnd = new Random();
        Game game = Game.getGame();
        int code = game.getLastDecisionCode();
        int raise = code + rnd.nextInt(2 * game.getMinBet());
        int money = getMoney();
        money -= raise;
        decision = raise;
        game.setLastDecisionCode(raise);
        game.addToBank(raise);
        setMoney(money);
    }

    public void bet() {
        Random rnd = new Random();
        Game game = Game.getGame();
        int prevBet = Math.max(decision, 0);
        int code = game.getLastDecisionCode();
        int bet;
        if (game.getPhase() == 1) {
            bet = game.getMinBet() / 2;
        } else if (game.getPhase() == 2) {
            bet = game.getMinBet();
        } else {
            bet = code > 0 ? code - prevBet : rnd.nextInt(game.getMinBet()) + game.getMinBet();
        }
        int money = getMoney();
        money -= bet;
        decision = bet + prevBet;
        game.setLastDecisionCode(decision);
        game.addToBank(bet);
        setMoney(money);
    }

    public void decide() {
        Random rnd = new Random();
        Game game = Game.getGame();
        int phase = game.getPhase();
        if (phase == 1) {
            bet();
            return;
        } else if (phase == 2) {
            bet();
            return;
        }
        defineCurrentCombination();
        defineExpectedCombinations();
        double p = rnd.nextDouble();
        if (phase == 3) {
            if (p < 0.1) {
                fold();
            } else if (p < 0.3) {
                raise();
            } else {
                bet();
            }
        } else {
            if (p < 0.1) {
                fold();
            } else if (p < 0.3) {
                raise();
            } else if (p < 0.5 && game.getLastDecisionCode() == 0) {
                check();
            } else {
                bet();
            }
        }
    }

    public int getDecisionCode() {
        return decision;
    }

    public String decision() {
        if (decision > 0) {
            return decision + "$";
        } else if (decision == 0) {
            return "check";
        } else {
            return "fold";
        }
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Image getPortrait() {
        return portrait;
    }

    public String getName() {
        return name;
    }

    public void dropCards() {
        card1 = null;
        card2 = null;
    }
}
