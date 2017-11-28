package com.gojava;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
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

    }

}
