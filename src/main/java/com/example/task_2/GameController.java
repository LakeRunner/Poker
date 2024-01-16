package com.example.task_2;

import com.example.task_2.Program.Card;
import com.example.task_2.Program.Game;
import com.example.task_2.Program.MyLists.MyLoopList;
import com.example.task_2.Program.Player;
import com.example.task_2.Program.Settings;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private record Place(double x, double y) {}

    @FXML
    private Button betButton;

    @FXML
    private Button checkButton;

    @FXML
    private Button foldButton;

    @FXML
    private Button raiseButton;

    @FXML
    private Label tipsLabel;

    @FXML
    private Canvas gameCanvas;

    @FXML
    private Button startButton;

    @FXML
    private Button endGameButton;

    private final Map<Player, Place> coordinates = new HashMap<>();
    private final double WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final double HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private final double[][] potentialCoordinates = {{WIDTH - 595, 425.0, 100.0, 425, WIDTH - 595, WIDTH - 250.0},
                                                     {HEIGHT - 248, HEIGHT - 248, HEIGHT / 2 - 101, 0.0, 0.0, HEIGHT / 2 - 101}};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyLoopList<Player> players = Game.getGame().getPlayers();
        for (int i = 0; i < players.size(); i++) {
            coordinates.put(players.get(i), new Place(potentialCoordinates[0][i], potentialCoordinates[1][i]));
        }

        deactivateGameButtons();
        startButton.setDisable(true);

        tipsLabel.setStyle(tipsLabel.getStyle() + "-fx-text-fill: yellow;");
        drawTable();
        drawSitPlaces();
        drawBank();
        drawDeckHolder();
        drawPlayer(Game.getGame().getPlayers().get(0));
        PauseTransition pause = new PauseTransition(Duration.seconds(0.4));
        pause.setOnFinished(event -> {
            drawPlayersInTurn(1);
            playSound(new File(Settings.getSettings().getSoundDirectory().getAbsolutePath() + "\\claps.wav"));
        });
        pause.play();
        // testDrawCard(gc);
    }

    public void activateGameButtons() {
        Game game = Game.getGame();
        if (game.getLastDecisionCode() <= 0) {
            checkButton.setDisable(false);
        } else {
            raiseButton.setDisable(false);
        }
        betButton.setDisable(false);
        foldButton.setDisable(false);
    }

    public void deactivateGameButtons() {
        betButton.setDisable(true);
        checkButton.setDisable(true);
        foldButton.setDisable(true);
        raiseButton.setDisable(true);
    }

    @FXML
    private void onStartButtonClick() {
        startButton.setDisable(true);
        startButton.setVisible(false);
        playSound(new File(Settings.getSettings().getSoundDirectory().getAbsolutePath() + "\\dealer.wav"));
        start();
    }

    @FXML
    private void onEndGameButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) endGameButton.getScene().getWindow();
        stage.setFullScreenExitHint("");
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    public void redraw() {
        gameCanvas.getGraphicsContext2D().clearRect(0, 0, WIDTH, HEIGHT);
        drawTable();
        drawSitPlaces();
        drawDeckHolder();
        drawPlayers();
        drawBank();
        drawCards();
    }

    private void drawTable() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#290807"));
        gc.fillRoundRect(WIDTH / 6, HEIGHT / 6, WIDTH / 1.5, HEIGHT / 1.5, 600, 600);
        gc.setFill(Color.GOLD);
        gc.fillRoundRect(WIDTH / 6 + 30, HEIGHT / 6 + 30, WIDTH / 1.5 - 60, HEIGHT / 1.5 - 60, 550, 550);
        gc.setFill(Color.valueOf("#215512"));
        gc.fillRoundRect(WIDTH / 6 + 40, HEIGHT / 6 + 40, WIDTH / 1.5 - 80, HEIGHT / 1.5 - 80, 550, 550);
        gc.setStroke(Color.valueOf("#609451"));
        gc.setLineWidth(7);
        gc.strokeRoundRect(WIDTH / 6 + 70, HEIGHT / 6 + 70, WIDTH / 1.5 - 140, HEIGHT / 1.5 - 140, 500, 500);
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(3);
        for (int i = 0; i < 5; i++) {
            gc.strokeRoundRect((WIDTH / 2 - 382) + 156 * i, HEIGHT / 2 - 100, 140, 193, 25, 25);
        }
    }

    private void drawSitPlaces() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#bf0a30"));
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fillOval(450, HEIGHT - 115, 100, 100);
        gc.strokeOval(450, HEIGHT - 115, 100, 100);

        gc.fillOval(WIDTH - 550, HEIGHT - 115, 100, 100);
        gc.strokeOval(WIDTH - 550, HEIGHT - 115, 100, 100);

        gc.fillOval(450, 15, 100, 100);
        gc.strokeOval(450, 15, 100, 100);

        gc.fillOval(WIDTH - 550, 15, 100, 100);
        gc.strokeOval(WIDTH - 550, 15, 100, 100);

        gc.fillOval(120, HEIGHT / 2 - 50, 100, 100);
        gc.strokeOval(120, HEIGHT / 2 - 50, 100, 100);

        gc.fillOval(WIDTH - 220, HEIGHT / 2 - 50, 100, 100);
        gc.strokeOval(WIDTH - 220, HEIGHT / 2 - 50, 100, 100);
    }

    public void drawPlayer(Player player) {
        Game game = Game.getGame();
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#bf0a30"));
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.setFont(new Font("Roboto Thin", 25));

        Image portrait = player.getPortrait();
        double x = coordinates.get(player).x;
        double y = coordinates.get(player).y;
        double a = 20;
        double width = 150;
        double height = 155;
        double lh = 45;
        double k = gc.getFont().getSize() / 3.5;
        String name = player.getName();

        gc.drawImage(portrait, x, y);
        gc.setFill(Color.valueOf("#545454"));
        if (player.equals(game.getCurrentPlayer())) {
            gc.setStroke(Color.RED);
        } else {
            gc.setStroke(Color.WHITE);
        }
        gc.fillRoundRect(x, y + height, width, lh, a, a);
        gc.strokeRoundRect(x, y + height, width, lh, a, a);
        gc.fillRoundRect(x, y + height + lh, width, lh, a, a);
        gc.strokeRoundRect(x, y + height + lh, width, lh, a, a);
        gc.setFill(Color.WHITE);
        gc.fillText(name, x + width / 2 - name.length() * 7, y + height + 32);
        String money = player.getMoney() + "$";
        gc.fillText(money, x + width / 2 - money.length() * k, y + height + 80);
        if (game.getPhase() >= 3 && player.getDecisionCode() != -1) {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            ImageView iv1 = new ImageView(player.equals(game.getMainPlayer()) ? player.getCard1().picture() : Card.reverse());
            iv1.setRotate(10);
            Image image1 = iv1.snapshot(params, null);
            ImageView iv2 = new ImageView(player.equals(game.getMainPlayer()) ? player.getCard2().picture() : Card.reverse());
            iv2.setRotate(-10);
            Image image2 = iv2.snapshot(params, null);
            int offset1 = player.equals(game.getMainPlayer()) ? -35 : -50;
            int offset2 = player.equals(game.getMainPlayer()) ? 25 : 10;
            gc.drawImage(image2, x + offset1, y - 40);
            gc.drawImage(image1, x + offset2, y - 40);
        }
        if (player.equals(Game.getGame().getDealer())) {
            gc.setFill(Color.BLUE);
            gc.fillOval(x + width, y + height + lh - 25, 50, 50);
            gc.setStroke(Color.CYAN);
            gc.strokeOval(x + width, y + height + lh - 25, 50, 50);
            gc.setFill(Color.SNOW);
            gc.fillText("D", x + width + 17, y + height + lh + 10);
        }
    }

    public void drawDeckHolder() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#E2BB7B"));
        gc.fillRect(WIDTH - 201, 15, 186, 242);
        gc.setFill(Color.valueOf("#996515"));
        gc.fillRect(WIDTH - 181, 35, 146, 202);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);
        gc.strokeLine(WIDTH - 201, 15, WIDTH - 181, 35);
        gc.strokeLine(WIDTH - 15, 15, WIDTH - 35, 35);
        gc.strokeLine(WIDTH - 201, 257, WIDTH - 181, 237);
        gc.strokeLine(WIDTH - 15, 257, WIDTH - 35, 237);

        Image image = Card.reverse();
        gc.drawImage(image, WIDTH - 186, 35);
    }

    public void drawBank() {
        Game game = Game.getGame();
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFont(new Font("Roboto Thin", 25));
        double k = gc.getFont().getSize() / 3.5;
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.WHITE);
        gc.fillRoundRect(WIDTH / 2 - 75, HEIGHT / 2 + 110, 150, 45, 20, 20);
        gc.strokeRoundRect(WIDTH / 2 - 75, HEIGHT / 2 + 110, 150, 45, 20, 20);
        gc.fillRoundRect(WIDTH / 2 - 75, HEIGHT / 2 + 155, 150, 45, 20, 20);
        gc.strokeRoundRect(WIDTH / 2 - 75, HEIGHT / 2 + 155, 150, 45, 20, 20);
        gc.setFill(Color.YELLOW);
        gc.fillText("Банк", WIDTH / 2 - 25, HEIGHT / 2 + 140);
        String bank = game.getBank() + "$";
        gc.fillText(bank, WIDTH / 2 - bank.length() * k, HEIGHT / 2 + 188);
    }

    public void drawCards() {
        Game game = Game.getGame();
        List<Card> cards = game.getTableCards();
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        if (game.getPhase() >= 4) {
            for (int i = 0; i < 3; i++) {
                gc.drawImage(cards.get(i).picture(), (WIDTH / 2 - 377) + 156 * i, HEIGHT / 2 - 95);
            }
        }
        if (game.getPhase() >= 5) {
            gc.drawImage(cards.get(3).picture(), WIDTH / 2 + 91, HEIGHT / 2 - 95);
        }
        if (game.getPhase() >= 6) {
            gc.drawImage(cards.get(4).picture(), WIDTH / 2 + 247, HEIGHT / 2 - 95);
        }
    }

    public void drawPlayers() {
        MyLoopList<Player> players = Game.getGame().getPlayers();
        for (Player player : players) {
            drawPlayer(player);
        }
    }

    public void drawPlayersInTurn(int index) {
        MyLoopList<Player> players = Game.getGame().getPlayers();
        Player player = players.get(index);
        PauseTransition innerPause = new PauseTransition(Duration.seconds(0.4));
        innerPause.setOnFinished(innerEvent -> {
            drawPlayer(player);
            if (index + 1 != players.size()) {
                drawPlayersInTurn(index + 1);
            } else {
                startButton.setDisable(false);
                String style = tipsLabel.getStyle();
                style = style.substring(0, style.indexOf("-fx-text-fill"));
                style += "-fx-text-fill: #00ff00";
                tipsLabel.setStyle(style);
                tipsLabel.setText("Начните игру!");
            }
        });
        innerPause.play();
    }

    public void showMainPlayerAction() {
        Game game = Game.getGame();
        String style = tipsLabel.getStyle();
        style = style.substring(0, style.indexOf("-fx-text-fill"));
        style += "-fx-text-fill: yellow";
        tipsLabel.setStyle(style);
        tipsLabel.setText("Вы: " + game.getMainPlayer().decision());
        redraw();
        deactivateGameButtons();
    }

    @FXML
    public void mainPlayerBet() {
        Game game = Game.getGame();
        Player player = game.getMainPlayer();
        player.bet();
        showMainPlayerAction();
        playPlayerDecisionSound(player);
        if (game.isPhaseComplete()) {
            game.increasePhase();
            redraw();
            game.setCurrentPlayer(game.getDealer());
        }
        game.nextPlayer();
        makeMoves();
    }

    @FXML
    public void mainPlayerCheck() {
        Game game = Game.getGame();
        Player player = game.getMainPlayer();
        player.check();
        showMainPlayerAction();
        playPlayerDecisionSound(player);
        if (game.isPhaseComplete()) {
            game.increasePhase();
            redraw();
            game.setCurrentPlayer(game.getDealer());
        }
        game.nextPlayer();
        makeMoves();
    }

    @FXML
    public void mainPlayerFold() {
        Game game = Game.getGame();
        Player player = game.getMainPlayer();
        player.fold();
        showMainPlayerAction();
        playPlayerDecisionSound(player);
        if (game.isPhaseComplete()) {
            game.increasePhase();
            redraw();
            game.setCurrentPlayer(game.getDealer());
        }
        game.nextPlayer();
        makeMoves();
    }

    @FXML
    public void mainPlayerRaise() {
        Game game = Game.getGame();
        Player player = game.getMainPlayer();
        player.raise();
        showMainPlayerAction();
        playPlayerDecisionSound(player);
        if (game.isPhaseComplete()) {
            game.increasePhase();
            redraw();
            game.setCurrentPlayer(game.getDealer());
        }
        game.nextPlayer();
        makeMoves();
    }

    private static void playPlayerDecisionSound(Player player) {
        String sound = Settings.getSettings().getSoundDirectory().getAbsolutePath() + "\\";
        int code = player.getDecisionCode();
        if (code > 0) {
            sound += "bet.wav";
        } else if (code < 0) {
            sound += "fold.wav";
        } else {
            sound += "check.wav";
        }
        playSound(new File(sound));
    }

    private static void playSound(File music) {
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

    public void start() {
        Game.getGame().setRandomDealer();

        String style = tipsLabel.getStyle();
        style = style.substring(0, style.indexOf("-fx-text-fill"));
        style += "-fx-text-fill: yellow";
        tipsLabel.setStyle(style);
        tipsLabel.setText("Выбираем дилера...");

        PauseTransition dealerSelection = new PauseTransition(Duration.seconds(1));
        dealerSelection.setOnFinished(innerEvent -> newRound());
        dealerSelection.play();
    }

    public void newRound() {
        Game game = Game.getGame();
        game.newRound();
        tipsLabel.setText("Ожидайте...");
        redraw();
        game.nextPlayer();
        makeMoves();
    }

    public void makeMoves() {
        Game game = Game.getGame();
        Player player = game.getCurrentPlayer();
        PauseTransition playerMove = new PauseTransition(Duration.seconds(2));
        playerMove.setOnFinished(innerEvent -> {
            if (!player.equals(game.getMainPlayer()) || game.getPhase() <= 2) {
                player.decide();
                tipsLabel.setText(player.getName() + ": " + player.decision());
                playPlayerDecisionSound(player);
                if (game.getPhase() < 3) {
                    game.increasePhase();
                    redraw();
                } else if (game.isPhaseComplete()) {
                    game.increasePhase();
                    redraw();
                    game.setCurrentPlayer(game.getDealer());
                } else {
                    redraw();
                }
                if (game.getActivePlayersCount() == 1 || game.getPhase() > 6) {
                    newRound();
                } else {
                    game.nextPlayer();
                    makeMoves();
                }
            } else {
                playSound(new File(Settings.getSettings().getSoundDirectory().getAbsolutePath() + "\\ring.wav"));
                String style = tipsLabel.getStyle();
                style = style.substring(0, style.indexOf("-fx-text-fill"));
                style += "-fx-text-fill: #00ff00";
                tipsLabel.setStyle(style);
                tipsLabel.setText("Ваш ход!");
                redraw();
                activateGameButtons();
            }
        });
        playerMove.play();
    }
}
