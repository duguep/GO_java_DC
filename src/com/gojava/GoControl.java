package com.gojava;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GoControl extends Control
{
    private GoBoard go_board;

    public GoControl()
    {
        setSkin(new GoControlSkin(this));
        go_board = new GoBoard();
        getChildren().add(go_board);



        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                go_board.placePiece(event.getX() - go_board.getOffset_h(), event.getY() - go_board.getOffset_v());
            }
        });

        // add a key listener that will reset the game
        setOnKeyPressed(new EventHandler<KeyEvent>() {
            // overridden handle method
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.SPACE)
                    go_board.resetGame();
            }
        });

    }
    GoBoard getGo_board()
    {
        return go_board;
    }
}
