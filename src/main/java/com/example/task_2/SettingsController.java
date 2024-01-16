package com.example.task_2;

import com.example.task_2.Program.Game;
import com.example.task_2.Program.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    protected ChoiceBox<String> themeChooser;

    @FXML
    protected Spinner<Integer> moneySpinner;

    @FXML
    protected Slider playersSlider;

    @FXML
    protected Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Settings settings = Settings.getSettings();
        themeChooser.getItems().addAll("Философы", "Музыканты");
        themeChooser.setValue(settings.getPlayersTheme());
        moneySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 5000, settings.getMoney(), 50));
        playersSlider.setValue(settings.getPlayersCount());

        themeChooser.setOnAction(event -> {
            settings.setPlayersTheme(themeChooser.getValue());
            Game.getGame().updatePlayersTheme();
        });
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setFullScreenExitHint("");
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    public void onPlayersSliderReleased() {
        Settings settings = Settings.getSettings();
        settings.setPlayersCount((int) playersSlider.getValue());
        Game.getGame().updatePlayersCount();
    }

    public void moneySpinnerValueChanged() {
        Settings settings = Settings.getSettings();
        settings.setMoney(moneySpinner.getValue());
        Game.getGame().updatePlayersMoney();
    }
}
