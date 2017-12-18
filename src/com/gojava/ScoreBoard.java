package com.gojava;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class ScoreBoard extends Pane
{
    Rectangle background;
    //Score
    GoPiece blackPiece;
    GoPiece whitePiece;
    Label scoreBlack;
    Label scoreWhite;

    GoBoard board;
    //Timer
    public  ScoreBoard(GoBoard board)
    {
        this.board = board;
        System.out.println("dede");
        background = new Rectangle();
        background.setFill(Color.valueOf("#757575"));

        blackPiece = new GoPiece(2);
        whitePiece = new GoPiece(1);

        scoreWhite = new Label("coucou");
        scoreBlack = new Label("ba");
        scoreWhite.setFont(new Font(25));
        getChildren().addAll(background, whitePiece, blackPiece, scoreBlack, scoreWhite);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        background.setHeight(height / 3.7);
        background.setWidth(width);
        blackPiece.resize(height/ 3.7, height/3.7);
        whitePiece.resize(height/ 3.7, height/3.7);
        scoreWhite.resize(width / 3.7, height/3.7);
        scoreWhite.setFont(new Font(height / 3.7));
    }

    @Override
    public void relocate(double x, double y)
    {
        super.relocate(0, 0);
        background.setX(0);
        background.setY(0);
        whitePiece.relocate(x - y / 16.3, 7);
        blackPiece.relocate(0, 7);
        scoreWhite.relocate(0 + y / 16.3, 0);
    }
}
