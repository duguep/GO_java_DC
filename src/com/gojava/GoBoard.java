package com.gojava;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;

public class GoBoard extends Pane
{
    private static final int GRID_SIZE = 7;

    private static final String BACKGROUND_PAINT = "#454545";
    private static final String STROKE_COLOR = "#ffffff";

    // rectangle that makes the background of the board
    private Rectangle background;

    // arrays for the lines that makeup the horizontal and vertical grid lines
    private Line[] horizontal;
    private Line[] vertical;

    // arrays holding translate objects for the horizontal and vertical grid lines
    private Translate[] horizontal_t;
    private Translate[] vertical_t;

    // arrays for the internal representation of the board and the pieces that are
    // in place
    private GoPiece[][] render;

    // the current player who is playing and who is his opposition
    private int current_player;
    private int opposing;

    // is the game currently in play
    private boolean in_play;

    // current scores of player 1 and player 2
    private int player1_score;
    private int player2_score;

    // the width and height of a cell in the board
    private double cell_width;
    private double cell_height;

    // 3x3 array that holds the pieces that surround a given piece
    private int[][] surrounding;

    // 3x3 array that determines if a reverse can be made in any direction
    private boolean[][] can_reverse;

    // offset for centering the grid;
    private double min;
    private double offset_h;
    private double offset_v;

    // text of cell
    Text t_h[];
    Text t_v[];
    public GoBoard()
    {
        horizontal = new Line[GRID_SIZE];
        vertical = new Line[GRID_SIZE];
        horizontal_t = new Translate[GRID_SIZE];
        vertical_t = new Translate[GRID_SIZE];
        render = new GoPiece[GRID_SIZE][GRID_SIZE];
        in_play = true;
        current_player = 2;
        opposing = 1;
        t_h =  new Text[GRID_SIZE];
        t_v =  new Text[GRID_SIZE];
        initialiseLinesBackground();
        initialiseRender();
    }

    // private method that will initialise the background and the lines
    private void initialiseLinesBackground() {
        background = new Rectangle();
        background.setFill(Color.valueOf(BACKGROUND_PAINT));
        getChildren().add(background);

        for (int i = 0; i < GRID_SIZE; ++i) {
            t_h[i] = new Text(50, 50, String.valueOf((char)(65 + i)));
            t_h[i].setFont(new Font(40));
            t_h[i].setTextAlignment(TextAlignment.CENTER);
            getChildren().add(t_h[i]);

            t_v[i] = new Text(50, 50, String.valueOf((char)((48 + t_v.length) - i)));
            t_v[i].setFont(new Font(40));
            t_v[i].setTextAlignment(TextAlignment.CENTER);
            getChildren().add(t_v[i]);


            horizontal[i] = new Line();
            horizontal[i].setStrokeWidth(2);
            horizontal[i].setStroke(Color.valueOf(STROKE_COLOR));
            horizontal[i].setStartX(0);
            horizontal[i].setStartY(0);
            horizontal[i].setEndY(0);
            vertical[i] = new Line();
            vertical[i].setStrokeWidth(2);
            vertical[i].setStroke(Color.valueOf(STROKE_COLOR));
            horizontal[i].setStartX(0);
            horizontal[i].setEndX(0);
            horizontal[i].setStartY(0);

            horizontal_t[i] = new Translate(0, 0);
            vertical_t[i] = new Translate(0, 0);

            horizontal[i].getTransforms().add(horizontal_t[i]);
            vertical[i].getTransforms().add(vertical_t[i]);

            getChildren().addAll(horizontal[i], vertical[i]);

        }
    }

    // overridden version of the resize method to give the board the correct size
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);

        min = 2 * Math.min(width, height) / 3;

        background.setWidth(width);
        background.setHeight(height);

        cell_width = min / 6;
        cell_height = min / 6;

        offset_h = (width - min) / 2;
        offset_v = (height - min) / 2;

        linesResizeRelocate(width, height);
        pieceResizeRelocate();

    }

    // private method for resizing and relocating all the lines

    private void linesResizeRelocate(double width, double height)
    {
        for (int i = 0; i < GRID_SIZE; ++i) {
            t_h[i].setX((offset_h + i * cell_height) - 10);
            t_h[i].setY(offset_v - 35);

            t_v[i].setX(offset_h - 60);
            t_v[i].setY((offset_v + i * cell_width) + 15);

            horizontal_t[i].setX(offset_h);
            horizontal_t[i].setY(offset_v + i * cell_height);
            horizontal[i].setEndX(6 * cell_width);

            vertical_t[i].setY(offset_v);
            vertical_t[i].setX(offset_h + i * cell_width);
            vertical[i].setEndY(6 * cell_height);
        }
    }

    // private method for resizing and relocating all the pieces

    private void pieceResizeRelocate() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < 7; ++j) {
                render[i][j].relocate(offset_h + i * cell_width - cell_width / 4, offset_v + j * cell_height - cell_height / 4);
                render[i][j].resize(cell_width, cell_height);
            }
        }
    }

    // private method that will initialise everything in the render array
    private void initialiseRender() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < 7; ++j) {
                render[i][j] = new GoPiece(0);
                getChildren().add(render[i][j]);
            }
        }
    }

    public void placePiece(final double x, final double y) {
        int cx = (int) Math.round(x / cell_width);
        int cy = (int) Math.round(y / cell_height);

        if (cx < 0) {
            cx = 0;
        } else if (cx > 6) {
            cx = 6;
        }

        if (cy < 0) {
            cy = 0;
        } else if (cy > 6) {
            cy = 6;
        }

        if (!in_play || render[cx][cy].getPiece() != 0)
            return;

        if (!checkArround(current_player, cx, cy))

        placeAndReverse(cx, cy);
        swapPlayers();

    }

    public boolean checkArround(int player, int x, int y)
    {
//        boolean isotherPlayer = false;
/*        if (x < 0 || x > 8 || y  < 0 || y > 8)
            return true;
        if (render[x -1][y].getPiece() == opposing &&
                render[x][y - 1].getPiece() == opposing &&
                render[x + 1][y].getPiece() == opposing &&
                render[x][y + 1].getPiece() == opposing)
            return true;
        return false;*/
        return false;
    }

    // private method for placing a piece and reversing pieces

    private void placeAndReverse(final int x, final int y) {
        render[x][y].setPiece(current_player);
    }

    public double getOffset_h() {
        return offset_h;
    }

    public double getOffset_v() {
        return offset_v;
    }

    private void swapPlayers() {
        current_player =  (current_player == 1) ? 2 : 1;
        opposing = (opposing == 1) ? 2 : 1;
    }

}
