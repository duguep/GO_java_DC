package com.gojava;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

public class GoPiece extends Group
{
    public GoPiece(int player)
    {
        this.player = player;
        t = new Translate();
        piece = new Ellipse();
        piece.getTransforms().add(t);
        if (this.player == 1)
        {
            piece.setStroke(Color.WHITE);
        }
        else if (this.player == 2)
        {
            piece.setStroke(Color.BLACK);
        }
        else
        {
            piece.setStroke(new Color(0, 0, 0, 0));
        }
        if (player != 0)
            getChildren().add(piece);

    }

    // overridden version of the resize method to give the piece the correct size
    @Override
    public void resize(double width, double height)
    {
        super.resize(width, height);
        piece.setCenterX(width / 2); piece.setCenterY(height / 2);
        piece.setRadiusX(3 * width / 8); piece.setRadiusY(3 * height / 8);
    }

    // overridden version of the relocate method to position the piece correctly
    @Override
    public void relocate(double x, double y)
    {
        super.relocate(x, y);
        t.setY(y);
        t.setX(x);
    }

    public void swapPiece()
    {
        getChildren().remove(piece);
        if(player == 1)
        {
            player = 2;
            piece.setStroke(Color.BLACK);
        }
        else if (player == 2)
        {
            player = 1;
            piece.setFill(Color.WHITE);
        }
        getChildren().add(piece);
    }

    public  int getPiece()
    {
        return player;
    }

    private int player;
    private Ellipse piece;
    private Translate t;
}
