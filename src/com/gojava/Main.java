package com.gojava;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application
{
    StackPane sp_mainlayout;
    GoControl gc_go;
    @Override
    public void init() throws Exception {
        sp_mainlayout = new StackPane();
        gc_go = new GoControl();
        sp_mainlayout.getChildren().add(gc_go);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Go");
        primaryStage.setScene(new Scene(sp_mainlayout, 800, 800));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {

    }
}
