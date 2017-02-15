package com.larryrun.texasplayer.gui.aigame;

import com.google.common.base.Joiner;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.larryrun.texasplayer.aigame.AIGameController;
import com.larryrun.texasplayer.aigame.AIGameModule;
import com.larryrun.texasplayer.gui.GUIUtils;
import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.event.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;

public class AIGameApp extends Application implements GameEventHandler {
    private AIGameController gameController;

    private List<PlayerInfoPane> playerInfoPanes;
    private VBox outerContainer;
    private boolean handStarted;
    private Button nextHandBtn;
    private Button foldBtn, callBtn, raiseBBBtn, raise3BBBtn, raiseAllBtn, raiseOKBtn;
    private TextField raiseAmountTextField;
    private Label publicCardsLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> GUIUtils.showException(e)));
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> GUIUtils.showException(e));

        try {
            outerContainer = new VBox();
            outerContainer.setPadding(new Insets(10));
            outerContainer.setSpacing(10);

            initGame();


            Scene scene = new Scene(outerContainer, 800, 600);
            primaryStage.setScene(scene);
            scene.getStylesheets().add(ClassLoader.getSystemResource("style/css/AppMain.css").toExternalForm());
            primaryStage.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGame() {
        outerContainer.getChildren().clear();
        playerInfoPanes = new ArrayList<>();
        handStarted = false;

        initGameOpBtn();
        initPlayerInfoPanes();
        initSharedCardsInfo();
        initSelfOpBtn();

        Injector injector = Guice.createInjector(new AIGameModule(this));
        gameController = injector.getInstance(AIGameController.class);
    }

    private void initGameOpBtn() {
        nextHandBtn = new Button("Next Hand");
        nextHandBtn.setOnAction(e -> {
            if(!handStarted) {
                nextHand();
                nextHandBtn.setDisable(true);
                handStarted = true;
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().add(nextHandBtn);

        outerContainer.getChildren().add(hBox);
    }

    private void initPlayerInfoPanes() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        int row = 0;
        for(; row < 2; row++) {
            PlayerInfoPane infoPane = new PlayerInfoPane("player" + (row + 1), gridPane, row);
            playerInfoPanes.add(infoPane);
        }

        PlayerInfoPane selfInfoPane = new PlayerInfoPane("self", gridPane, row);
        playerInfoPanes.add(selfInfoPane);

        outerContainer.getChildren().add(gridPane);
    }

    private void initSharedCardsInfo() {
        publicCardsLabel = new Label();

        outerContainer.getChildren().add(publicCardsLabel);
    }

    private void initSelfOpBtn() {
        foldBtn = new Button("FOLD");
        foldBtn.setOnAction(e -> {
            gameController.getPlayerControllerHuman().setNextDecision(BettingDecision.FOLD);
            gameController.getAIGameGameHandController().playHumanMove();
        });
        callBtn = new Button("CALL");
        callBtn.setOnAction(e -> {
            gameController.getPlayerControllerHuman().setNextDecision(BettingDecision.CALL);
            gameController.getAIGameGameHandController().playHumanMove();
        });
        raiseBBBtn = new Button("1BB");
        raiseBBBtn.setOnAction(e -> {
            gameController.getPlayerControllerHuman().setNextDecision(BettingDecision.raise(-1));
            gameController.getAIGameGameHandController().playHumanMove();
        });
        raise3BBBtn = new Button("3BB");
        raiseAllBtn = new Button("All IN");
        raiseOKBtn = new Button("RAISE");

        HBox hBox = new HBox();
        hBox.setSpacing(15);
        hBox.getChildren().add(foldBtn);
        hBox.getChildren().add(callBtn);
//      only support fixed raise for now

        hBox.getChildren().add(raiseBBBtn);
//        hBox.getChildren().add(raise3BBBtn);
//        hBox.getChildren().add(raiseAllBtn);
//        raiseAmountTextField = new TextField();
//        raiseAmountTextField.setAlignment(Pos.CENTER_RIGHT);
//        hBox.getChildren().add(raiseAmountTextField);
//        hBox.getChildren().add(raiseOKBtn);
        outerContainer.getChildren().add(hBox);
    }

    private void nextHand() {
        playerInfoPanes.forEach(playerInfoPane -> {
            playerInfoPane.setDealer(false);
            playerInfoPane.setBB(false);
            playerInfoPane.setSB(false);
            playerInfoPane.setWinner(false);
            playerInfoPane.setFrontMoney(0);
            playerInfoPane.setOnTurn(false);
        });
        publicCardsLabel.setText("");
        new Thread(() -> gameController.play()).start();
    }

    private PlayerInfoPane getHumanPlayerInfoPane() {
        return playerInfoPanes.get(playerInfoPanes.size() - 1);
    }

    @Override
    public void handleGameEvent(GameEvent gameEvent) {
        Platform.runLater(()->{
            switch (gameEvent.eventName()) {
                case GameHandStarted.EVENT_NAME:
                    GameHandStarted gameHandStarted = (GameHandStarted)gameEvent;
                    setDealer(gameHandStarted.getDealer());
                    break;
                case BBTaken.EVENT_NAME:
                    BBTaken bbTaken = (BBTaken) gameEvent;
                    playerInfoPanes.get(bbTaken.getPlayer().getNumber() - 1).setBB(true);
                    playerInfoPanes.get(bbTaken.getPlayer().getNumber() - 1).addFrontMoney(bbTaken.getAmount());
                    break;
                case SBTaken.EVENT_NAME:
                    SBTaken sbTaken = (SBTaken) gameEvent;
                    playerInfoPanes.get(sbTaken.getPlayer().getNumber() - 1).setSB(true);
                    playerInfoPanes.get(sbTaken.getPlayer().getNumber() - 1).addFrontMoney(sbTaken.getAmount());
                    break;
                case HoleCardsDealt.EVENT_NAME:
                    HoleCardsDealt holeCardsDealt = (HoleCardsDealt) gameEvent;
                    if(holeCardsDealt.getPlayer().isHumanPlayer()) {
                        getHumanPlayerInfoPane().setHoleCardInfo(holeCardsDealt.getCards().get(0) + " " + holeCardsDealt.getCards().get(1));
                    }else {
                        playerInfoPanes.get(holeCardsDealt.getPlayer().getNumber() - 1).setHoleCardInfo("--- ---");
                    }
                    break;
                case FlopCardsDealt.EVENT_NAME:
                    FlopCardsDealt flopCardsDealt = (FlopCardsDealt) gameEvent;
                    appendToPublicCardsLabel(flopCardsDealt.getCards());
                    break;
                case TurnCardDealt.EVENT_NAME:
                    TurnCardDealt turnCardDealt = (TurnCardDealt) gameEvent;
                    appendToPublicCardsLabel(Collections.singletonList(turnCardDealt.getCard()));
                    break;
                case RiverCardDealt.EVENT_NAME:
                    RiverCardDealt riverCardDealt = (RiverCardDealt) gameEvent;
                    appendToPublicCardsLabel(Collections.singletonList(riverCardDealt.getCard()));
                    break;
                case PlayerOnTurn.EVENT_NAME:
                    PlayerOnTurn playerOnTurn = (PlayerOnTurn) gameEvent;
                    setPlayerOnTurn(playerOnTurn.getPlayer());
                    break;
                case BetPlaced.EVENT_NAME:
                    BetPlaced betPlaced = (BetPlaced) gameEvent;
                    playerInfoPanes.get(betPlaced.getPlayer().getNumber() - 1).showBettingDecision(betPlaced.getBettingDecision());
                    break;
                case PlayerCreated.EVENT_NAME:
                    PlayerCreated playerCreated = (PlayerCreated) gameEvent;
                    playerInfoPanes.get(playerCreated.getPlayer().getNumber() - 1).setBalance(playerCreated.getPlayer().getMoney());
                    break;
                case PlayerBalanceChanged.EVENT_NAME:
                    PlayerBalanceChanged playerBalanceChanged = (PlayerBalanceChanged) gameEvent;
                    playerInfoPanes.get(playerBalanceChanged.getPlayer().getNumber() - 1).setBalance(playerBalanceChanged.getPlayer().getMoney());
                    break;
                case HandCompleted.EVENT_NAME:
                    HandCompleted handCompleted = (HandCompleted) gameEvent;
                    for(Player winner: handCompleted.getWinners()) {
                        playerInfoPanes.get(winner.getNumber() - 1).setWinner(true);
                    }
                    nextHandBtn.setDisable(false);
                    break;
            }
        });
    }

    private void setDealer(Player player) {
        playerInfoPanes.get(player.getNumber() - 1).setDealer(true);
    }

    private void appendToPublicCardsLabel(List<Card> s) {
        publicCardsLabel.setText(publicCardsLabel.getText() + " " + Joiner.on(" ").join(s));
    }

    private void setPlayerOnTurn(Player player) {
        playerInfoPanes.forEach(playerInfoPane -> playerInfoPane.setOnTurn(false));
        playerInfoPanes.get(player.getNumber() - 1).setOnTurn(true);
    }

}
