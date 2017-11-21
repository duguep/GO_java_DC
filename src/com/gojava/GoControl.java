package com.gojava;

import javafx.scene.control.Control;

public class GoControl extends Control
{
    private GoBoard go_board;

    public GoControl()
    {
        setSkin(new GoControlSkin(this));
        go_board = new GoBoard();
        getChildren().add(go_board);
    }
}
