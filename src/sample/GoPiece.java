package sample;

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

    public int getPiece()
    {
        return player;
    }

    private int player;
    private Ellipse piece;
    private Translate t;
}
