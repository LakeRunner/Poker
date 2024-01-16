package com.example.task_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    protected Label pokerLabel;

    @FXML
    protected Button playButton;

    @FXML
    protected Button settingsButton;

    @FXML
    protected Button exitButton;

    @FXML
    public void initialize() {

    }

    @FXML
    protected void onPlayButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.setFullScreenExitHint("");
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    @FXML
    protected void onSettingsButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("settings.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) settingsButton.getScene().getWindow();
        stage.setFullScreenExitHint("");
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    @FXML
    protected void onExitButtonClick() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}