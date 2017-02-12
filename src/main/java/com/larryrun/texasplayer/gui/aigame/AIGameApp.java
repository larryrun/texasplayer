package com.larryrun.texasplayer.gui.aigame;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.larryrun.texasplayer.aigame.AIGameController;
import com.larryrun.texasplayer.aigame.PlayerControllerHuman;
import com.larryrun.texasplayer.aigame.AIGameModule;
import com.larryrun.texasplayer.gui.GUIUtils;
import com.larryrun.texasplayer.model.event.GameEvent;
import com.larryrun.texasplayer.model.event.GameEventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AIGameApp extends Application implements GameEventHandler {
    private AIGameController gameController;
    private PlayerControllerHuman playerControllerHuman;

    private List<PlayerInfoPane> playerInfoPanes;
    private VBox outerContainer;
    private boolean gameStarted;
    private Button gameStartResetBtn;
    private Button foldBtn, callBtn, raiseBBBtn, raise3BBBtn, raiseAllBtn, raiseOKBtn;
    private TextField raiseAmountTextField;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> GUIUtils.showErrorAlert(e.getMessage())));
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> GUIUtils.showErrorAlert(e.getMessage()));

        outerContainer = new VBox();
        outerContainer.setPadding(new Insets(10));
        outerContainer.setSpacing(10);

        initGame();

        Scene scene = new Scene(outerContainer, 1024, 800);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(ClassLoader.getSystemResource("style/css/AppMain.css").toExternalForm());
        primaryStage.show();
    }

    private void initGame() {
        outerContainer.getChildren().clear();
        playerInfoPanes = new ArrayList<>();
        gameStarted = false;

        initGameOpBtn();
        initPlayerInfoPanes();
        initSelfOpBtn();
    }

    private void initGameOpBtn() {
        gameStartResetBtn = new Button("Start Game");
        gameStartResetBtn.setOnAction(e -> {
            if(gameStarted) {
                initGame();
            }else {
                gameStartResetBtn.setDisable(true);
                startGame();
                gameStarted = true;
                gameStartResetBtn.setText("Reset Game");
                gameStartResetBtn.setDisable(false);
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().add(gameStartResetBtn);

        outerContainer.getChildren().add(hBox);
    }

    private void initPlayerInfoPanes() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        int row = 0;
        for(; row < 1; row++) {
            PlayerInfoPane infoPane = new PlayerInfoPane("player" + (row + 1), gridPane, row);
            playerInfoPanes.add(infoPane);
        }

        PlayerInfoPane selfInfoPane = new PlayerInfoPane("self", gridPane, row);
        playerInfoPanes.add(selfInfoPane);

        outerContainer.getChildren().add(gridPane);
    }

    private void initSelfOpBtn() {
        foldBtn = new Button("FOLD");
        callBtn = new Button("CALL");
        raiseBBBtn = new Button("BB");
        raise3BBBtn = new Button("3BB");
        raiseAllBtn = new Button("All IN");
        raiseOKBtn = new Button("RAISE");

        HBox hBox = new HBox();
        hBox.setSpacing(15);
        hBox.getChildren().add(foldBtn);
        hBox.getChildren().add(callBtn);
//      only support fixed raise for now

//        hBox.getChildren().add(raiseBBBtn);
//        hBox.getChildren().add(raise3BBBtn);
//        hBox.getChildren().add(raiseAllBtn);
//        raiseAmountTextField = new TextField();
//        raiseAmountTextField.setAlignment(Pos.CENTER_RIGHT);
//        hBox.getChildren().add(raiseAmountTextField);
        hBox.getChildren().add(raiseOKBtn);
        outerContainer.getChildren().add(hBox);
    }

    private void startGame() {
        Injector injector = Guice.createInjector(new AIGameModule(this));
        gameController = injector.getInstance(AIGameController.class);
        playerControllerHuman = injector.getInstance(PlayerControllerHuman.class);

        gameController.play();
    }

    @Override
    public void handleGameEvent(GameEvent gameEvent) {

    }
}
