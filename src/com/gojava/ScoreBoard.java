package com.gojava;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class ScoreBoard extends Pane
{
    Rectangle background;
    //Score
    GoPiece blackPiece;
    GoPiece whitePiece;
    Label scoreBlack;
    Label scoreWhite;

    public GoBoard board;

    //round timer
    private static final Integer STARTTIME = 120;
    private Timeline timeline;
    private Label timerLabel;
    private Integer timeSeconds = STARTTIME;

    public  ScoreBoard(GoBoard board)
    {

        this.board = board;
        background = new Rectangle();
        background.setFill(Color.valueOf("#757575"));

        blackPiece = new GoPiece(2);
        whitePiece = new GoPiece(1);

        scoreWhite = new Label("0");
        scoreBlack = new Label("0");
        scoreWhite.setFont(new Font(25));
        getChildren().addAll(background, whitePiece, blackPiece, scoreBlack, scoreWhite);
        initialiseTimer();
    }

    private void initialiseTimer()
    {
        timeline = new Timeline();
        timerLabel = new Label();
        timeSeconds = STARTTIME;
        timerLabel.setText(timeSeconds.toString());
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setStyle("-fx-font-size: 4em;");
        if (timeline != null)
            timeline.stop();
        timeSeconds = STARTTIME;
        timerLabel.setText(timeSeconds.toString());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeSeconds--;
                Integer min = (timeSeconds / 60) % 60;
                if (timeSeconds > 60)
                    timerLabel.setText(min.toString() + "." + timeSeconds % 60);
                else
                    timerLabel.setText(timeSeconds.toString());
                if(timeSeconds <= 0)
                {
                    board.swapPlayers();
                    timerLabel.setTextFill(Color.WHITE);
                }
                else if (timeSeconds <= 60 && timeSeconds > 15)
                    timerLabel.setTextFill(Color.YELLOW);
                else if (timeSeconds <= 15)
                    timerLabel.setTextFill(Color.RED);

            }
        }));
        timeline.playFromStart();
        getChildren().add(timerLabel);
    }


    @Override
    public void resize(double width, double height)
    {
        double pourcent = height * 15 / 100;
        super.resize(width, height);
        background.setHeight(pourcent + 5);
        background.setWidth(width);
        blackPiece.resize(pourcent, pourcent);
        whitePiece.resize(pourcent, pourcent);
        scoreWhite.resize(pourcent, pourcent);
        scoreWhite.setFont(new Font(pourcent));
        scoreBlack.resize(pourcent, pourcent);
        scoreBlack.setFont(new Font(pourcent));
        timerLabel.resize(pourcent, pourcent);
        timerLabel.setFont(new Font(pourcent));
    }

    @Override
    public void relocate(double x, double y)
    {
        super.relocate(0, 0);
        background.setX(0);
        background.setY(0);
        whitePiece.relocate(x - (x * 15 / 100), 2);
        blackPiece.relocate(x * 15 / 100, 2);
        scoreWhite.relocate((x * 15 / 100) * 2, 0);
        scoreBlack.relocate(x - ((x * 15 / 100) * 2), 0);
        timerLabel.relocate(x/2 - timerLabel.getWidth(), 0);
    }

    public void resetTime()
    {
        timeSeconds = STARTTIME;
    }

    public void changeScore(int scoreWhite, int scoreBlack)
    {
        this.scoreWhite.setText(""+scoreWhite);
        this.scoreBlack.setText(""+scoreBlack);
    }
}
