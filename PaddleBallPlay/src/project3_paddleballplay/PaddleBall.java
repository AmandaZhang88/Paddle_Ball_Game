/*
A label or a text that displays the score that is 20 at game start, changes with each point lost until it is zero at game end.
For every 10 paddle connections in a row, the ball moves faster and gets smaller if it got larger
For every 5 paddle connections in a row, the ball changes color
For every 3 paddle misses in a row, the paddle grows in length and the ball gets larger
 */
package project3_paddleballplay;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.animation.KeyFrame;

public class PaddleBall extends Pane {

    // set witdth and height depend on the game screen form Project3_PaddleBallPlay file
    public int screen_w = Project3_PaddleBallPlay.screen_width;
    public int screen_h = Project3_PaddleBallPlay.screen_height;

    // set unChange width and height for the rectangle paddle
    public final int PADDLE_WIDTH = 100;
    public final int PADDLE_HEIGHT = 15;

    public double radius = 16;              // ball radius
    private double x = radius, y = radius;  // the cricle started position
    private double dx = 1, dy = 1;
    int score = 20;
    int bump = 0;                           //count the connection between the paddle and ball
    int miss = 0;                          // count the

    // set label paddle rectangle and cricle ball property
    public Label showChallenge = new Label();
    public Label startLabel = new Label();
    public Circle ball = new Circle(x, y, radius);

    public Rectangle paddle = new Rectangle(screen_w / 2, screen_h - PADDLE_HEIGHT * 2,
            PADDLE_WIDTH, PADDLE_HEIGHT);

    // create a new Text for holding the score
    private Text scorePrint = new Text("Score = " + score);
    private Text gameOver = new Text("Game over!");

    private Timeline animation;

    private boolean former_corner_bump = false;

    public PaddleBall() {
        // Set ball
        ball.setFill(Color.DEEPSKYBLUE);
        ball.setStroke(Color.BLACK);

        // set a Label for score print
        paddle.setFill(Color.BLACK);      // paddle styles set
        setStyle("-fx-border-color: black; -fx-background-color: MOCCASIN");
        scorePrint.setStyle("-fx-font-weight:bold");
        scorePrint.setFill(Color.BLACK);
        scorePrint.setFont(Font.font(25));
        scorePrint.setX(screen_w - 150);  // x -= 150. x-aixs position   
        scorePrint.setY(30);              // (300,30)
        // set for start Label
        startLabel = new Label("Press 'Enter' to start the game !");
        startLabel.setStyle("-fx-background-color:PINK");
        startLabel.setFont(Font.font(25));
        startLabel.setLayoutX((screen_w - 300) / 2);
        startLabel.setLayoutY(screen_h / 2);

        // adding all the item to the game screen
        getChildren().addAll(ball, paddle, scorePrint, gameOver, startLabel);

        // set for a action of click the button
        // pressed enter Key, Game will start
        setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                runGame();
                gameOver.setText(null);  //the showChallenge will be null, then the game running
                startLabel.setText(null);
            }
        });
        // set the border for paddle moving 
        // the paddle will moves on the pane, paddles' y-aixs never change
        setOnMouseMoved(e -> {
            paddle.setX(e.getX());
            if (paddle.getX() <= 0) {
                paddle.setX(0);
            } else if (paddle.getX() + paddle.getWidth() >= getWidth()) {
                paddle.setX(getWidth() - paddle.getWidth());
            }
        });

        // set timeline animation
        animation = new Timeline(
                new KeyFrame(Duration.millis(5), e -> moveBall()));
        animation.setCycleCount(Timeline.INDEFINITE);
        System.out.println(animation.getRate());   // see the intilization speed
    }

    public void runGame() {     // run game when this function is called 
        animation.play();
    }

    protected void moveBall() {
        //Check boundaries and if  it out of boundes
        //multiply by -1  Change ball move direction     
        if (x < radius || x > getWidth() - radius) {
            dx *= -1;
        }
        if (y < radius) {    // hit the top
            dy *= -1;        // Change ball move direction  
        }
        /*check the connection between the paddle and ball
        if  collided ,bump++ , increase 
        if  no collide, bump--, increase 
         */
        if ((((y + radius) >= paddle.getY()) // ?????  I am still working on the corner case,                                               
                && paddle.getX() <= x + radius // if the ball bump the Corner of retangle, there are more calculattion and setting 
                && paddle.getX() + paddle.getWidth() >= x - radius) & (dy > 0)) {   // dy > 0 to check the relative motion position
            dy *= -1;
            bump++;

            System.out.println(" bump inside" + bump + " Times ");
            miss = 0;  // reset miss counter, if any miss, start count the bump again
            // For every 10 paddle connections in a row, 
            // the ball moves faster and gets smaller if it got larger
            if ((bump % 10 == 0) && (bump > 0)) {
                increaseSpeed();               // speed up ++
                ballSizeDecrease();            // ball size --
            }
            // For every 5 paddle connections in a row,
            // the ball changes color
            if ((bump % 5 == 0) && (bump > 0)) {
                changeColor();
            }
            System.out.println(" bump ouside " + bump);
        }
        // A showChallenge or a text that displays the score that is 20 at game start,
        // changes with each point lost until it is zero at game end.
        // For every 3 paddle misses in a row, the paddle grows in length and the ball gets larger 
        if (y + radius >= screen_h) {
            score--;       // If the ball misses the paddle, then the score is decremented by 1.     
            miss++;        // if the ball misses the paddle , then the miss++

            System.out.println("  miss " + miss);

            bump = 0;     // reset bump counter, if thewe are anty bump, count the miss start over
            // every 3 paddle misses in a row, the paddle grows in length and the ball gets larger
            if (miss % 3 == 0) {
                this.showChallenge.setText(null);     // make challenge message disappear
                GrowPaddleLength();
                ballSizeIncrease();
            }
            //If score is less than zero, pause the animation game
            // display "Game Over" messgae
            if (score <= 0) {
                animation.pause();
                gameOver.setFill(Color.BLUEVIOLET);
                gameOver.setText("Game over !");
                gameOver.setX((screen_w - 100) / 2);
                gameOver.setY(screen_h / 2);
                gameOver.setFont(Font.font(30));
            }
            // show score of game
            scorePrint.setText("Score = " + score);
            // set the x and y back to the initial position for restarting game
            x = radius;
            y = radius;
            ball.setCenterX(x);
            ball.setCenterY(y);
        }
        x += dx;
        y += dy;
        ball.setCenterX(x);
        ball.setCenterY(y);
    }

    // This function increased the speed after 10 connections are made between the ball and paddle
    // showChallenge "speeding up" is displayed-shows user that the game is speeding
    // ball changes color
    public void increaseSpeed() {
        // set a showChallenge tell player, the speed and size changed
        showChallenge = new Label("Speed and Ball Size Challenge");
        showChallenge.setStyle("-fx-background-color:YELLOW");
        showChallenge.setLayoutX(screen_w - 180);
        showChallenge.setLayoutY(50);
        getChildren().add(showChallenge);
        // increase the speed of the ball 0.25 times faster
        animation.setRate(animation.getRate() + 0.5);

        // make sure the highest speed not more then 5
        // if the speed > 5, set speed = 5, otherwise, no change from last step
        animation.setRate(animation.getRate() > 8 ? 8 : animation.getRate());
    }

    // To decrease the speed  
    // only if the speed over 0.75, too fast, then will slow
    public void decreaseSpeed() {
        animation.setRate(animation.getRate() > 8 ? animation.getRate() - 0.5 : animation.getRate());
    }

    public void ballSizeDecrease() {
        this.radius = radius / 2;    //  new radius
        if (this.radius < 3) // make sure is visitable
        {
            this.radius = 3;
        }
        ball.setRadius(this.radius);   // new ball. set radius
    }

    public void ballSizeIncrease() {
        this.radius = radius + 10;
        ball.setRadius(this.radius);
    }

    // the ball changes color by random
    public void changeColor() {
        Color randomColor = new Color(Math.random(), Math.random(), Math.random(), 1.0);
        this.ball.setFill(randomColor);
    }

    // For every 3 paddle misses in a row, 
    // the paddle grows in length and the ball gets larger  
    protected void GrowPaddleLength() {
        paddle.setWidth(paddle.getWidth() + 35);
    }

    public DoubleProperty rateProperty() {
        return animation.rateProperty();
    }

    public void pause() {             // pause the game when function is called
        animation.pause();
    }
}
