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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Stream;

public class AIGameApp extends Application implements GameEventHandler {
    private AIGameController gameController;

    private Map<String, PlayerInfoPane> playerInfoPaneMap;
    private VBox outerContainer;
    private GridPane playerInfoGridPane;
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

            Scene scene = new Scene(outerContainer, 600, 450);
            primaryStage.setScene(scene);
            scene.getStylesheets().add(ClassLoader.getSystemResource("style/css/AppMain.css").toExternalForm());
            primaryStage.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGame() {
        outerContainer.getChildren().clear();
        playerInfoPaneMap = new HashMap<>();
        handStarted = false;

        initGameOpBtn();
        initPlayerInfoPanes();
        initSharedCardsInfo();
        initSelfOpBtn();

        disableActionControls();

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
        playerInfoGridPane = new GridPane();
        playerInfoGridPane.setHgap(10);
        playerInfoGridPane.setVgap(5);

        outerContainer.getChildren().add(playerInfoGridPane);
    }

    private void initSharedCardsInfo() {
        publicCardsLabel = new Label();

        outerContainer.getChildren().add(publicCardsLabel);
    }

    private void initSelfOpBtn() {
        foldBtn = new Button("FOLD");
        foldBtn.setOnAction(e ->
                runInNewThread(() -> {
                    disableActionControls();
                    gameController.getPlayerControllerHuman().setNextDecisionToFold();
                    gameController.getAIGameGameHandController().playHumanMove();
                })
        );
        callBtn = new Button("CALL");
        callBtn.setOnAction(e ->
                runInNewThread(() -> {
                    disableActionControls();
                    gameController.getPlayerControllerHuman().setNextDecisionToCall();
                    gameController.getAIGameGameHandController().playHumanMove();
                })
        );
        raiseBBBtn = new Button("1BB");
        raiseBBBtn.setOnAction(e ->
                runInNewThread(() -> {
                    disableActionControls();
                    gameController.getPlayerControllerHuman().setNextDecisionToRaiseBB();
                    gameController.getAIGameGameHandController().playHumanMove();
                })
        );
        raise3BBBtn = new Button("3BB");
        raise3BBBtn.setOnAction(e ->
                runInNewThread(() -> {
                    disableActionControls();
                    gameController.getPlayerControllerHuman().setNextDecisionToRaise3BB();
                    gameController.getAIGameGameHandController().playHumanMove();
                })
        );

        raiseAllBtn = new Button("All IN");
        raiseOKBtn = new Button("RAISE");
        raiseOKBtn.setOnAction((e ->
                runInNewThread(() -> {
                    disableActionControls();
                    int amount = Integer.parseInt(raiseAmountTextField.getText());
                    gameController.getPlayerControllerHuman().setNextDecision(BettingDecision.raise(amount));
                    gameController.getAIGameGameHandController().playHumanMove();
                })
        ));

        HBox hBox = new HBox();
        hBox.setSpacing(15);
        hBox.getChildren().add(foldBtn);
        hBox.getChildren().add(callBtn);
//      only support fixed raise for now

        hBox.getChildren().add(raiseBBBtn);
        hBox.getChildren().add(raise3BBBtn);
//        hBox.getChildren().add(raiseAllBtn);
        raiseAmountTextField = new TextField();
        raiseAmountTextField.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(raiseAmountTextField);
        hBox.getChildren().add(raiseOKBtn);
        outerContainer.getChildren().add(hBox);
    }

    private void nextHand() {
        playerInfoPaneMap.forEach((name, playerInfoPane) -> {
            playerInfoPane.clearStatus();
        });
        publicCardsLabel.setText("");
        runInNewThread(() -> gameController.play());
    }

    private void runInNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public void handleGameEvent(GameEvent gameEvent) {
        Platform.runLater(()->{
            switch (gameEvent.eventName()) {
                case PlayerCreated.EVENT_NAME: {
                    PlayerCreated playerCreated = (PlayerCreated) gameEvent;
                    int row = 0;
                    for (Player player : playerCreated.getPlayer()) {
                        PlayerInfoPane infoPane = new PlayerInfoPane(player.getName(), playerInfoGridPane, row++);
                        playerInfoPaneMap.put(player.getName(), infoPane);
                    }
                    break;
                }
                case PlayerBalanceChanged.EVENT_NAME: {
                    PlayerBalanceChanged playerBalanceChanged = (PlayerBalanceChanged) gameEvent;
                    getInfoPaneFromPlayer(playerBalanceChanged.getPlayer()).setBalance(playerBalanceChanged.getPlayer().getMoney());
                    break;
                }
                case GameHandStarted.EVENT_NAME: {
                    GameHandStarted gameHandStarted = (GameHandStarted) gameEvent;
                    setDealer(gameHandStarted.getDealer());
                    getInfoPaneFromPlayer(gameHandStarted.getBbPlayer()).setBB(true);
                    getInfoPaneFromPlayer(gameHandStarted.getSbPlayer()).setSB(true);
                    break;
                }
                case HoleCardsDealt.EVENT_NAME: {
                    HoleCardsDealt holeCardsDealt = (HoleCardsDealt) gameEvent;
                    if (holeCardsDealt.getPlayer().isHumanPlayer()) {
                        getInfoPaneFromPlayer(holeCardsDealt.getPlayer()).setHoleCardInfo(holeCardsDealt.getCards().get(0) + " " + holeCardsDealt.getCards().get(1));
                    } else {
                        getInfoPaneFromPlayer(holeCardsDealt.getPlayer()).setHoleCardInfo("--- ---");
                    }
                    break;
                }
                case FlopCardsDealt.EVENT_NAME: {
                    FlopCardsDealt flopCardsDealt = (FlopCardsDealt) gameEvent;
                    appendToPublicCardsLabel(flopCardsDealt.getCards());
                    break;
                }
                case TurnCardDealt.EVENT_NAME: {
                    TurnCardDealt turnCardDealt = (TurnCardDealt) gameEvent;
                    appendToPublicCardsLabel(Collections.singletonList(turnCardDealt.getCard()));
                    break;
                }
                case RiverCardDealt.EVENT_NAME: {
                    RiverCardDealt riverCardDealt = (RiverCardDealt) gameEvent;
                    appendToPublicCardsLabel(Collections.singletonList(riverCardDealt.getCard()));
                    break;
                }
                case PlayerOnTurn.EVENT_NAME: {
                    PlayerOnTurn playerOnTurn = (PlayerOnTurn) gameEvent;
                    setPlayerOnTurn(playerOnTurn.getPlayer());
                    break;
                }
                case BetPlaced.EVENT_NAME: {
                    BetPlaced betPlaced = (BetPlaced) gameEvent;
                    getInfoPaneFromPlayer(betPlaced.getPlayer()).addBetMoney(betPlaced.getAmount());
                    break;
                }
                case DecisionMade.EVENT_NAME: {
                    DecisionMade decisionMade = (DecisionMade) gameEvent;
                    getInfoPaneFromPlayer(decisionMade.getPlayer()).showBettingDecision(decisionMade.getBettingDecision());
                    break;
                }
                case HandCompleted.EVENT_NAME: {
                    HandCompleted handCompleted = (HandCompleted) gameEvent;
                    for (Player winner : handCompleted.getWinners()) {
                        getInfoPaneFromPlayer(winner).setWinner(true);
                    }
                    for(Player showDownPlayer : handCompleted.getShowdownPlayers()) {
                        getInfoPaneFromPlayer(showDownPlayer).setHoleCardInfo(showDownPlayer.getHoleCards().get(0) + " " + showDownPlayer.getHoleCards().get(1));
                    }
                    disableActionControls();
                    nextHandBtn.setDisable(false);
                    handStarted = false;
                    break;
                }
            }
        });
    }

    private void setDealer(Player player) {
        playerInfoPaneMap.forEach((name, infoPane) -> infoPane.setDealer(false));
        getInfoPaneFromPlayer(player).setDealer(true);
    }

    private void appendToPublicCardsLabel(List<Card> s) {
        publicCardsLabel.setText(publicCardsLabel.getText() + " " + Joiner.on(" ").join(s));
    }

    private void setPlayerOnTurn(Player player) {
        playerInfoPaneMap.forEach((name, playerInfoPane) -> playerInfoPane.setOnTurn(false));
        getInfoPaneFromPlayer(player).setOnTurn(true);

        if(player.isHumanPlayer()) {
            enableActionControls();
        }
    }

    private void disableActionControls() {
        Stream.of(foldBtn, callBtn, raiseBBBtn, raise3BBBtn, raiseAllBtn, raiseOKBtn, raiseAmountTextField).filter(Objects::nonNull).forEach(control -> {control.setDisable(true);});
    }

    private void enableActionControls() {
        Stream.of(foldBtn, callBtn, raiseBBBtn, raise3BBBtn, raiseAllBtn, raiseOKBtn, raiseAmountTextField).filter(Objects::nonNull).forEach(control -> {control.setDisable(false);});
    }

    private PlayerInfoPane getInfoPaneFromPlayer(Player player) {
        return playerInfoPaneMap.get(player.getName());
    }
}
