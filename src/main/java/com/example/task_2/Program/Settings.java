package com.example.task_2.Program;

import java.io.File;

public class Settings {

    private int playersCount;
    private int money;
    private File playersPortraitsDirectory;
    private String playersTheme;
    private final File soundDirectory;
    private final File mainPlayerPortrait;
    private final File cardsDirectory;
    private static final Settings settings = new Settings();

    private Settings() {
        soundDirectory = new File("target\\classes\\com\\example\\task_2\\other\\sound");
        cardsDirectory = new File("target\\classes\\com\\example\\task_2\\other\\pictures\\cards");
        mainPlayerPortrait = new File("target\\classes\\com\\example\\task_2\\other\\pictures\\players\\player.jpg");
        setDefault();
    }

    public void setDefault() {
        playersTheme = "Философы";
        playersPortraitsDirectory = new File("target\\classes\\com\\example\\task_2\\other\\pictures\\players\\philosphers");
        playersCount = 6;
        money = 1000;
    }

    public static Settings getSettings() {
        return settings;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public int getMoney() {
        return money;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setPlayersTheme(String playersTheme) {
        if (!playersTheme.equals(this.playersTheme)) {
            if (playersTheme.equals("Музыканты")) {
                setPlayersPortraitsDirectory(new File("target\\classes\\com\\example\\task_2\\other\\pictures\\players\\musicians"));
            } else if (playersTheme.equals("Философы")) {
                setPlayersPortraitsDirectory(new File("target\\classes\\com\\example\\task_2\\other\\pictures\\players\\philosphers"));
            }
        }
        this.playersTheme = playersTheme;
    }

    public File getCardsDirectory() {
        return cardsDirectory;
    }

    public String getPlayersTheme() {
        return playersTheme;
    }

    public File getPlayersPortraitsDirectory() {
        return playersPortraitsDirectory;
    }

    private void setPlayersPortraitsDirectory(File playersPortraitsDirectory) {
        this.playersPortraitsDirectory = playersPortraitsDirectory;
    }

    public File getMainPlayerPortrait() {
        return mainPlayerPortrait;
    }

    public File getSoundDirectory() {
        return soundDirectory;
    }
}
