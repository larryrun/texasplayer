package com.larryrun.texasplayer.gui.consultant;

import com.larryrun.texasplayer.gui.GUIUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultantApp extends Application {
    private List<PlayerInfoInput> playerInfoInputs = new ArrayList<>(12);

    private Pane outerContainer;
    private TextField smallBlindField;
    private Button gameStartBtn;

    private Button callBtn;
    private Button foldBtn;
    private Button raiseBtn;
    private TextField raiseField;

    private TextField[] publicCardsFields;
    private Button publicCardConfirmBtn;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> GUIUtils.showErrorAlert(e.getMessage())));
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> GUIUtils.showErrorAlert(e.getMessage()));

        primaryStage.setTitle("Texas Player");
        outerContainer = initOuterContainer();
        initGameMetaCtrl();
        initPlayerAndLogCtrl();
        gameStartBtn = new Button("Game Start!");
        gameStartBtn.setOnAction(e -> gameStart());
        outerContainer.getChildren().add(gameStartBtn);
        initPlayerOpCtrl();
        initPublicCardsCtrl();

        Scene scene = new Scene(outerContainer, 1024, 800);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(ClassLoader.getSystemResource("style/css/AppMain.css").toExternalForm());
        primaryStage.show();
    }

    private void dealerSelected(PlayerInfoInput moveInput) {
        playerInfoInputs.stream().filter(e -> e != moveInput).forEach(e -> e.setDealer(false));
    }

    private Pane initOuterContainer() {
        VBox outerContainer = new VBox();
        outerContainer.setPadding(new Insets(10));
        outerContainer.setSpacing(10);
        return outerContainer;
    }

    private void initGameMetaCtrl() {
        GridPane gameMetaCtrlContainer = new GridPane();
        outerContainer.getChildren().add(gameMetaCtrlContainer);

        gameMetaCtrlContainer.add(new Label("Small Blind: "), 0, 0);

        smallBlindField = new TextField();
        gameMetaCtrlContainer.add(smallBlindField, 1, 0);
    }

    private void initPlayerAndLogCtrl() {
        HBox container = new HBox();
        outerContainer.getChildren().add(container);

        GridPane playerCtrlGrid = new GridPane();
        container.getChildren().add(playerCtrlGrid);

        playerCtrlGrid.setAlignment(Pos.TOP_LEFT);
        playerCtrlGrid.setHgap(10);
        playerCtrlGrid.setVgap(10);

        int rowIdx = 0;
        PlayerInfoInput selfInfoInput = PlayerInfoInput.createPlayerMoveInputIn(playerCtrlGrid, rowIdx++, "Self", this::dealerSelected, true);
        playerInfoInputs.add(selfInfoInput);

        for(int i = 1; i < 12; i++) {
            PlayerInfoInput playerInfoInput = PlayerInfoInput.createPlayerMoveInputIn(playerCtrlGrid, rowIdx++, "Player" + i, this::dealerSelected, false);
            playerInfoInputs.add(playerInfoInput);

            //only enables 5 players at initial time
            if(i > 4) {
                playerInfoInput.setDisabled(true);
            }
        }

    }

    private void initPlayerOpCtrl() {
        HBox container = new HBox();
        container.setSpacing(5);
        outerContainer.getChildren().add(container);

        callBtn = new Button("Call");
        container.getChildren().add(callBtn);

        foldBtn = new Button("Fold");
        container.getChildren().add(foldBtn);

        raiseField = new TextField();
        raiseField.setPromptText("Raise Amount");
        raiseField.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().add(raiseField);

        raiseBtn = new Button("Raise");
        raiseBtn.setOnAction(e -> {
        });
        container.getChildren().add(raiseBtn);
    }

    private void initPublicCardsCtrl() {
        HBox container = new HBox();
        container.setSpacing(5);
        outerContainer.getChildren().add(container);

        publicCardsFields = new TextField[5];
        for(int i = 0; i < 5;i++) {
            TextField publicCardField = publicCardsFields[i] = new TextField();
            container.getChildren().add(publicCardField);
        }
        publicCardsFields[0].setPromptText("Flop 1");
        publicCardsFields[1].setPromptText("Flop 2");
        publicCardsFields[2].setPromptText("Flop 3");
        publicCardsFields[3].setPromptText("Turn");
        publicCardsFields[4].setPromptText("River");

        publicCardConfirmBtn = new Button("OK");
        outerContainer.getChildren().add(publicCardConfirmBtn);
    }

    private PlayerInfoInput getDealer() {
        Optional<PlayerInfoInput> optional = playerInfoInputs.stream().filter(PlayerInfoInput::isDealer).findFirst();
        if(optional.isPresent()) {
            return optional.get();
        }else {
            throw new RuntimeException("No Dealer is selected!");
        }
    }

    private void gameStart() {
        PlayerInfoInput playerInfoInput = getDealer();
    }

}
