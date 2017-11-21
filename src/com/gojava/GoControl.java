package com.gojava;

import javafx.scene.control.Control;

public class GoControl extends Control
{
    public GoControl()
    {
        setSkin(new GoControlSkin(this));
    }
}
