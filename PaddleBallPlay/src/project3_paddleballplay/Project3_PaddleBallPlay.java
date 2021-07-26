/* This is a game - Paddle-ball for One
 CSC 239 project 3   Fengnan Zhang
 */
package project3_paddleballplay;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import project3_paddleballplay.PaddleBall;

/**
 *
 * @author Fengnan Zhang
 */
public class Project3_PaddleBallPlay extends Application {

    // variables need for the game screen
    public static int screen_width = 760;
    public static int screen_height = 400;
    public boolean gameStarted = false;
    //Button buStart = new Button("Start Play");

    @Override
    public void start(Stage primaryStage) {
        PaddleBall gameScreen = new PaddleBall();

        gameScreen.moveBall();   // move ball, start game 
        Scene scene = new Scene(gameScreen, screen_width, screen_height);
        primaryStage.setTitle(" Paddle - ball for One ");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        gameScreen.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
