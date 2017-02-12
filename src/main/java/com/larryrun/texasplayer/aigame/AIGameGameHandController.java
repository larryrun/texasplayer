package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.HandPowerRanker;
import com.larryrun.texasplayer.controller.HandStrengthEvaluator;
import com.larryrun.texasplayer.controller.StatisticsController;
import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.model.*;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.utils.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AIGameGameHandController {
    protected final Logger logger;
    private final HandPowerRanker handPowerRanker;
    private final GameProperties gameProperties;
    private final StatisticsController statisticsController;
    private final HandStrengthEvaluator handStrengthEvaluator;
    private final OpponentModeler opponentModeler;

    @Inject
    public AIGameGameHandController(final Logger logger,
                                    final HandPowerRanker handPowerRanker,
                                    final GameProperties gameProperties,
                                    final StatisticsController statisticsController,
                                    final HandStrengthEvaluator handStrengthEvaluator,
                                    final OpponentModeler opponentModeler) {
        this.logger = logger;
        this.handPowerRanker = handPowerRanker;
        this.gameProperties = gameProperties;
        this.statisticsController = statisticsController;
        this.handStrengthEvaluator = handStrengthEvaluator;
        this.opponentModeler = opponentModeler;
    }

    public void play(Game game) {
        GameHand gameHand = createGameHand(game);

        Boolean haveWinner = false;
        while (!gameHand.getBettingRoundName().equals(
                BettingRoundName.POST_RIVER)
                && !haveWinner) {
            haveWinner = playRound(gameHand);
        }

        if (!haveWinner) {
            showDown(gameHand);
        }
    }

    protected GameHand createGameHand(Game game) {
        GameHand gameHand = new GameHand(game.getPlayers());
        game.addGameHand(gameHand);
        return gameHand;
    }

    private GameHand currentGameHand;
    private int toPlay;
    private int turn;
    private int numberOfPlayersAtBeginningOfRound;
    protected Boolean playRound(GameHand gameHand) {
        currentGameHand = gameHand;
        gameHand.nextRound();

        toPlay = gameHand.getPlayersCount();
        if (gameHand.getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            takeBlinds(gameHand);
            toPlay--; // Big blinds don't have to call on himself if no raise :)
        }

        turn = 1;
        numberOfPlayersAtBeginningOfRound = gameHand.getPlayersCount();
        // Check if we have a winner
        return false;
    }

    public void playAIMoveUntilHumanPlayerTurn() {
        while (toPlay > 0) {
            Player player = currentGameHand.getNextPlayer();
            if(player.isHumanPlayer()) {
                break;
            }

            BettingDecision bettingDecision = player.decide(currentGameHand);

            // We can't raise at second turn
            if (turn > numberOfPlayersAtBeginningOfRound && bettingDecision.isRaise()) {
                bettingDecision = BettingDecision.CALL;
            }

            // After a raise, every active players after the raiser must play
            if (bettingDecision.isRaise()) {
                toPlay = currentGameHand.getPlayersCount() - 1;
            }

            applyDecision(currentGameHand, player, bettingDecision);
            turn++;
            toPlay--;
        }

        if (currentGameHand.getPlayersCount() == 1) {
            Player winner = currentGameHand.getCurrentPlayer();
            winner.addMoney(currentGameHand.getTotalBets());
        }
    }

    public void playerHumanMove(BettingDecision humanBettingDecision) {
        Player humanPlayer = currentGameHand.getCurrentPlayer();
        PlayerControllerHuman playerControllerHuman = (PlayerControllerHuman) humanPlayer.getPlayerController();

        // We can't raise at second turn
        if (turn > numberOfPlayersAtBeginningOfRound && humanBettingDecision.isRaise()) {
            humanBettingDecision = BettingDecision.CALL;
        }

        // After a raise, every active players after the raiser must play
        if (humanBettingDecision.isRaise()) {
            toPlay = currentGameHand.getPlayersCount() - 1;
        }

        applyDecision(currentGameHand, humanPlayer, humanBettingDecision);
        turn++;
        toPlay--;

        playAIMoveUntilHumanPlayerTurn();
    }

    private void takeBlinds(GameHand gameHand) {
        Player smallBlindPlayer = gameHand.getNextPlayer();
        Player bigBlindPlayer = gameHand.getNextPlayer();

        gameHand.getCurrentBettingRound().placeBet(smallBlindPlayer, gameProperties.getSmallBlind());
        gameHand.getCurrentBettingRound().placeBet(bigBlindPlayer, gameProperties.getBigBlind());
    }

    private void applyDecision(GameHand gameHand, Player player, BettingDecision bettingDecision) {
        double handStrength = handStrengthEvaluator.evaluate(player.getHoleCards(), gameHand.getSharedCards(), gameHand.getPlayersCount());
        gameHand.applyDecision(player, bettingDecision, gameProperties, handStrength);
    }

    private List<Player> getWinners(GameHand gameHand) {
        Iterable<Player> activePlayers = gameHand.getPlayers();
        List<Card> sharedCards = gameHand.getSharedCards();

        HandPower bestHandPower = null;
        List<Player> winners = new ArrayList<>();
        for (Player player : activePlayers) {
            List<Card> mergeCards = new ArrayList<>(player.getHoleCards());
            mergeCards.addAll(sharedCards);
            HandPower handPower = handPowerRanker.rank(mergeCards);

            if (bestHandPower == null || handPower.compareTo(bestHandPower) > 0) {
                winners.clear();
                winners.add(player);
                bestHandPower = handPower;
            } else if (handPower.equals(bestHandPower)) {
                winners.add(player);
            }
        }
        statisticsController.storeWinners(winners);
        return winners;
    }

    private void showDown(GameHand gameHand) {
        List<Player> winners = getWinners(gameHand);

        // Gains
        int gain = gameHand.getTotalBets() / winners.size();
        int modulo = gameHand.getTotalBets() % winners.size();
        for (Player winner : winners) {
            int gainAndModulo = gain;
            if (modulo > 0) {
                gainAndModulo += modulo;
            }
            winner.addMoney(gainAndModulo);
            modulo--;
        }

        // Opponent modeling
        opponentModeler.save(gameHand);
    }
}
