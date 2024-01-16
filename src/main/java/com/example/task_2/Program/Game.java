package com.example.task_2.Program;

import com.example.task_2.Program.MyLists.MyLoopList;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Game {

    private int bank;
    private int lastDecisionCode;
    private int activePlayersCount;
    private final List<Card> deck = new ArrayList<>();
    private final List<Card> tableCards;
    private final MyLoopList<Player> players = new MyLoopList<>();
    private final Player mainPlayer;
    private Player dealer;
    private Player currentPlayer;
    private int phase;
    private final static Game game = new Game();

    private Game() {
        bank = 0;
        phase = 1;
        activePlayersCount = players.size();
        tableCards = new ArrayList<>();
        Settings settings = Settings.getSettings();
        int money = settings.getMoney();
        InputStream stream = null;
        try {
            stream = new FileInputStream(settings.getMainPlayerPortrait());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image mainPlayerPortrait = new Image(Objects.requireNonNull(stream));
        mainPlayer = new Player("Вы", money, mainPlayerPortrait);
        players.add(mainPlayer);
        for (Card.Suit suit : Card.Suit.values()) {
            for (int i = 2; i < 15; i++) {
                Image face = null;
                try {
                    String name = i + "_" + suit + ".png";
                    face = new Image(new FileInputStream(Settings.getSettings().getCardsDirectory() + "\\" + name));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                deck.add(new Card(suit, i, face));
            }
        }
        addRivals();
        setTableCards();
    }

    public void nextPlayer() {
        int index = players.indexOf(currentPlayer) + 1;
        Player player = players.get(index);
        if (player.getDecisionCode() != -1) {
            setCurrentPlayer(player);
        } else {
            findNextPlayer(index + 1);
        }
    }

    private void findNextPlayer(int index) {
        Player player = players.get(index);
        if (player.getDecisionCode() != -1) {
            setCurrentPlayer(player);
        } else {
            findNextPlayer(index + 1);
        }
    }

    public MyLoopList<Player> getPlayers() {
        return players;
    }

    public static Game getGame() {
        return game;
    }

    public void updatePlayersCount() {
        try {
            Settings settings = Settings.getSettings();
            int playersCount = settings.getPlayersCount();
            if (players.size() < playersCount) {
                File[] rivals = settings.getPlayersPortraitsDirectory().listFiles();
                Set<String> names = new HashSet<>();
                for (Player player : players) {
                    names.add(player.getName());
                }
                for (File rival : Objects.requireNonNull(rivals)) {
                    String str = rival.getName();
                    int index = str.indexOf(".");
                    String name = str.substring(0, index);
                    int money = settings.getMoney();
                    if (!names.contains(name)) {
                        InputStream stream = new FileInputStream(rival);
                        Image portrait = new Image(Objects.requireNonNull(stream));
                        players.add(new Player(name,  money, portrait));
                    }
                    if (players.size() == playersCount) {
                        break;
                    }
                }
            } else if (players.size() > playersCount) {
                while (players.size() != playersCount) {
                    players.remove(players.size() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decrementActivePlayersCount() {
        activePlayersCount--;
    }

    public int getActivePlayersCount() {
        return activePlayersCount;
    }

    public void updatePlayersTheme() {
        while (players.size() != 1) {
            players.remove(1);
        }
        addRivals();
    }

    private void addRivals() {
        try {
            Settings settings = Settings.getSettings();
            File dir = settings.getPlayersPortraitsDirectory();
            int rivalsCount = settings.getPlayersCount() - 1;
            int money = settings.getMoney();
            List<File> rivals = Arrays.asList(Objects.requireNonNull(dir.listFiles()));
            Collections.shuffle(rivals);
            for (int i = 0; i < rivalsCount; i++) {
                File rival = rivals.get(i);
                String str = rival.getName();
                int pointIndex = str.indexOf(".");
                String name = str.substring(0, pointIndex);
                InputStream stream = new FileInputStream(rival);
                Image portrait = new Image(Objects.requireNonNull(stream));
                players.add(new Player(name,  money, portrait));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Card> getTableCards() {
        return tableCards;
    }

    public void setTableCards() {
        tableCards.clear();
        tableCards.add(deck.get(players.size() * 2 + 1));
        tableCards.add(deck.get(players.size() * 2 + 2));
        tableCards.add(deck.get(players.size() * 2 + 3));
        tableCards.add(deck.get(players.size() * 2 + 4));
        tableCards.add(deck.get(players.size() * 2 + 5));
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public int getBank() {
        return bank;
    }

    public void addToBank(int money) {
        setBank(getBank() + money);
    }

    public void updatePlayersMoney() {
        Settings settings = Settings.getSettings();
        int money = settings.getMoney();
        for (Player player : players) {
            player.setMoney(money);
        }
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDealer(Player dealer) {
        this.dealer = dealer;
        currentPlayer = dealer;
    }

    public void setRandomDealer() {
        Random rnd = new Random();
        int index = rnd.nextInt(players.size());
        dealer = players.get(index);
    }

    public Player getDealer() {
        return dealer;
    }

    public Player getMainPlayer() {
        return mainPlayer;
    }

    public int getPhase() {
        return phase;
    }

    public void increasePhase() {
        phase++;
        if (phase >= 4) {
            setLastDecisionCode(0);
            for (Player player : players) {
                if (player.getDecisionCode() != -1) {
                    player.resetDecision();
                }
            }
        }
    }

    public int getMinBet() {
        return 5;
    }

    public void shuffleDeck() {
        Collections.shuffle(getDeck());
    }

    public void setPlayersCards() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setCard1(deck.get(i));
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setCard2(deck.get(i + players.size()));
        }
    }

    public boolean isPhaseComplete() {
        int code = -1;
        for (Player player : players) {
            if (player.getDecisionCode() == -2) {
                return false;
            }
            if (player.getDecisionCode() >= 0) {
                if (code == -1) {
                    code = player.getDecisionCode();
                } else {
                    if (code != player.getDecisionCode()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setLastDecisionCode(int lastDecisionCode) {
        this.lastDecisionCode = lastDecisionCode;
    }

    public int getLastDecisionCode() {
        return lastDecisionCode;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void newRound() {
        players.forEach(player -> {
            if (player.getMoney() <= 0) {
                players.remove(player);
            } else {
                player.resetDecision();
            }
        });
        activePlayersCount = players.size();
        shuffleDeck();
        setTableCards();
        if (!players.contains(getDealer())) {
            setRandomDealer();
        } else {
            setDealer(players.get(players.indexOf(getDealer()) + 1));
        }
        phase = 1;
        players.setBeginning(players.indexOf(getDealer()) + 1);
        setCurrentPlayer(getDealer());
        setPlayersCards();
    }
}
