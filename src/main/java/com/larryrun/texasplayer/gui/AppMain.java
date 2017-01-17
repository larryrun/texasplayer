package com.larryrun.texasplayer.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppMain extends Application {
    private final static Logger LOG = LoggerFactory.getLogger(AppMain.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Texas Player");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcome");
        grid.add(sceneTitle, 0, 0, 2, 1);

        CheckBox selfEnableCheckBox = new CheckBox();
        selfEnableCheckBox.setSelected(true);
        selfEnableCheckBox.setDisable(true);
        grid.add(selfEnableCheckBox, 0, 1);
        grid.add(new Label("Self: "), 1, 1);

        CheckBox player1EnableCheckBox = new CheckBox();
        grid.add(player1EnableCheckBox, 0, 2);
        grid.add(new Label("Player1: "), 1,2);

        TextField player1NameField = new TextField();
        grid.add(player1NameField, 2, 2);

        TextField player1RaiseField = new TextField();
        grid.add(player1RaiseField, 3, 2);

        Button player1RaiseBtn = new Button("Raise");
        grid.add(player1RaiseBtn, 4, 2);

        Button player1CallBtn = new Button("Call");
        grid.add(player1CallBtn, 5, 2);

        Button player1FoldBtn = new Button("Fold");
        grid.add(player1FoldBtn, 6, 2);


        Scene scene = new Scene(grid, 1024, 800);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(ClassLoader.getSystemResource("style/css/AppMain.css").toExternalForm());
        primaryStage.show();
    }
}
