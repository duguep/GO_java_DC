package com.gojava;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


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

    //round timer
    private static final Integer STARTTIME = 120;
    private Timeline timeline;
    private Label timerLabel;
    private Integer timeSeconds = STARTTIME;

    ScoreBoard bla;

    // Variable to check if the case has been captured
    private boolean hasCaptured;

    private List<GoPiece> pieceChunk;
    private boolean chunkHasLiberty;

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
        initialiseTimer();
        System.out.println("scoreboard");
        bla = new ScoreBoard(this);
        getChildren().add(bla);
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
                    swapPlayers();
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

    // public method for resetting the game
    public void resetGame() {
        resetRenders();
        in_play = true;
        current_player = 2;
        opposing = 1;
        player1_score = 2;
        player2_score = 2;
    }

    // private method that will reset the renders
    private void resetRenders() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                render[i][j].setPiece(0);
            }
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

        timerLabel.resize(50, 50);
        timerLabel.relocate(width/2 - timerLabel.getWidth(), 0);

        linesResizeRelocate(width, height);
        pieceResizeRelocate();
        bla.relocate(width, height);
        bla.resize(width, height);
    }

    // private method for resizing and relocating all the lines

    private void linesResizeRelocate(double width, double height) {
        for (int i = 0; i < GRID_SIZE; ++i) {
            t_h[i].setX((offset_h + i * cell_height) - 10);
            t_h[i].setY(offset_v - 40);
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
                render[i][j].relocate(offset_h + i * cell_width - (3 * cell_width / 8), offset_v + j * cell_height - (3 * cell_height / 8));
                render[i][j].resize(cell_width, cell_height);
            }
        }
    }

    // private method that will initialise everything in the render array
    private void initialiseRender() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < 7; ++j) {
                render[i][j] = new GoPiece(0);
                render[i][j].setX(i);
                render[i][j].setY(j);
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

        if (!in_play)
            return;

        if (getPiece(cx, cy) != 0)
            return;

        System.out.println("liberties: " + countLiberties(cx, cy));

        if (!isValidMove(cx, cy))
            return;

        render[cx][cy].setPiece(current_player);
        hasCaptured = false;
        checkCapture(cx + 1, cy, opposing);
        checkCapture(cx - 1, cy, opposing);
        checkCapture(cx, cy + 1, opposing);
        checkCapture(cx, cy - 1, opposing);
        swapPlayers();

        /*
            placeAndReverse(cx, cy);
            swapPlayers();
        */

    }

    public boolean isValidMove(int x, int y) {
        if (render[x][y].getPiece() != 0)
            return (false);
        if (countLiberties(x, y) == 0) {
            hasCaptured = false;
            render[x][y].setPiece(current_player);
            checkCapture(x + 1, y, opposing);
            checkCapture(x - 1, y, opposing);
            checkCapture(x, y + 1, opposing);
            checkCapture(x, y - 1, opposing);
            render[x][y].setPiece(0);
            if (hasCaptured)
                return (true);

            render[x][y].setPiece(current_player);
            if (!checkChunk(x, y, current_player))
            {
                render[x][y].setPiece(0);
                return (false);
            }

            int[] surroundPieces = new int[4];
            surroundPieces[0] = getPiece(x + 1, y);
            surroundPieces[1] = getPiece(x - 1, y);
            surroundPieces[2] = getPiece(x, y + 1);
            surroundPieces[3] = getPiece(x, y - 1);
            if ((surroundPieces[0] == current_player || surroundPieces[0] == -1)
                    && (surroundPieces[1] == current_player || surroundPieces[1] == -1)
                    && (surroundPieces[2] == current_player || surroundPieces[2] == -1)
                    && (surroundPieces[3] == current_player || surroundPieces[3] == -1))
                return (true);
            return (false);
        }
        return (true);
    }

    private void checkCapture(final int x, final int y, final int player) {
        if (!checkChunk(x, y, player)) {
            for (GoPiece piece : pieceChunk) {
                hasCaptured = true;
                piece.setPiece(0);
            }
        }
    }

    private boolean checkChunk(final int x, final int y, final int player) {
        pieceChunk = new ArrayList<GoPiece>();
        chunkHasLiberty = false;

        if (getPiece(x, y) == player) {
            pieceChunk.add(render[x][y]);
        }
        for (int i = 0; i < pieceChunk.size(); ++i) {
            checkPiece(pieceChunk.get(i).getX(), pieceChunk.get(i).getY(), player);
        }
        return (chunkHasLiberty);
    }

    private void checkPiece(final int x, final int y, final int player) {
        if (!validCoords(x, y))
            return;

        if (checkPosition(x + 1, y, player))
            chunkHasLiberty = true;
        if (checkPosition(x - 1, y, player))
            chunkHasLiberty = true;
        if (checkPosition(x, y + 1, player))
            chunkHasLiberty = true;
        if (checkPosition(x, y - 1, player))
            chunkHasLiberty = true;
    }

    private boolean validCoords(final int x, final int y) {
        if ((x >= 0 && x < GRID_SIZE + 1) && (y >= 0 && y < GRID_SIZE + 1)) {
            return (true);
        }
        return (false);
    }

    private boolean checkPosition(final int x, final int y, final int player) {
        if (!validCoords(x, y))
            return (false);
        int pieceType = getPiece(x, y);
        GoPiece piece = render[x][y];
        if (pieceType == 0)
            return (true);
        if (pieceType == player && !pieceChunk.contains(piece))
            pieceChunk.add(piece);
        return (false);
    }

    private int countLiberties(final int x, final int y)
    {
        int liberties = 0;

        if (x > 0 && (getPiece(x - 1, y) == 0 || getPiece(x - 1, y) == current_player))
            ++liberties;
        if (x < 6 && (getPiece(x + 1, y) == 0 || getPiece(x + 1, y) == current_player))
            ++liberties;
        if (y > 0 && (getPiece(x, y - 1) == 0 || getPiece(x, y - 1) == current_player))
            ++liberties;
        if (y < 6 && (getPiece(x, y + 1) == 0 || getPiece(x,y + 1) == current_player))
            ++liberties;

        return liberties;
    }

    private int getPiece(int x, int y) {
        return render[x][y].getPiece();
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
        timeSeconds = STARTTIME;
    }
    int getCurrent_player()
    {
        return current_player;
    }
}
