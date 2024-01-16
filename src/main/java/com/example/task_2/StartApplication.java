package com.example.task_2;

import com.example.task_2.Program.Combination;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.File;
import java.io.IOException;

import java.util.Random;

public class StartApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Poker!");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        Random rnd = new Random();
        File menu_music = new File("src\\main\\resources\\com\\example\\task_2\\other\\music\\menu_music");
        File[] content = menu_music.listFiles();
        // playMenuMusic(content[rnd.nextInt(content.length)]);
        launch();
    }


    private static void playMenuMusic(File music) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}