package com.gojava;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class GoBoard extends Pane
{

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

    public GoBoard()
    {
        horizontal = new Line[9];
        vertical = new Line[9];
        horizontal_t = new Translate[9];
        vertical_t = new Translate[9];
        initialiseLinesBackground();
    }

    // private method that will initialise the background and the lines
    private void initialiseLinesBackground() {
        background = new Rectangle();
        background.setFill(Color.valueOf(BACKGROUND_PAINT));
        getChildren().add(background);

        for (int i = 0; i < 9; ++i) {
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

        min = 3 * Math.min(width, height) / 4;

        background.setWidth(width);
        background.setHeight(height);

        cell_width = min / 8;
        cell_height = min / 8;

        offset_h = (width - min) / 2;
        offset_v = (height - min) / 2;

        linesResizeRelocate(width, height);
        pieceResizeRelocate();

    }

    // private method for resizing and relocating all the lines

    private void linesResizeRelocate(double width, double height) {
        for (int i = 0; i < 9; ++i) {
            horizontal_t[i].setX(offset_h);
            horizontal_t[i].setY(offset_v + i * cell_height);
            horizontal[i].setEndX(8 * cell_width);

            vertical_t[i].setY(offset_v);
            vertical_t[i].setX(offset_h + i * cell_width);
            vertical[i].setEndY(8 * cell_height);
        }
    }

    // private method for resizing and relocating all the pieces

    private void pieceResizeRelocate() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                //render[i][j].relocate(offset_h + i * cell_width + cell_width / 8, offset_v + j * cell_height + cell_height / 8);
                //render[i][j].resize(cell_width, cell_height);
            }
        }
    }

    // private method that will initialise everything in the render array
    private void initialiseRender() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                render[i][j] = new GoPiece(0);
                getChildren().add(render[i][j]);
            }
        }
    }

}
