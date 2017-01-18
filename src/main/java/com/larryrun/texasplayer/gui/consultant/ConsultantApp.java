package com.larryrun.texasplayer.gui.consultant;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConsultantApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
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

        PlayerMoveInput player1MoveInput = PlayerMoveInput.createPlayerMoveInputIn(grid, 2, "Player1");
        PlayerMoveInput player2MoveInput = PlayerMoveInput.createPlayerMoveInputIn(grid, 3, "Player2");
        PlayerMoveInput player3MoveInput = PlayerMoveInput.createPlayerMoveInputIn(grid, 4, "Player3");
        PlayerMoveInput player4MoveInput = PlayerMoveInput.createPlayerMoveInputIn(grid, 5, "Player4");

        Scene scene = new Scene(grid, 1024, 800);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(ClassLoader.getSystemResource("style/css/AppMain.css").toExternalForm());
        primaryStage.show();
    }
}
