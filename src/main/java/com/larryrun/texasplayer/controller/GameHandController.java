package com.larryrun.texasplayer.controller;

import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.model.*;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.utils.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GameHandController {
    protected final Logger logger;
    private final HandPowerRanker handPowerRanker;
    protected final GameProperties gameProperties;
    private final StatisticsController statisticsController;
    private final HandStrengthEvaluator handStrengthEvaluator;
    private final OpponentModeler opponentModeler;
    protected final GameEventDispatcher gameEventDispatcher;

    @Inject
    public GameHandController(final Logger logger,
                              final HandPowerRanker handPowerRanker,
                              final GameProperties gameProperties,
                              final StatisticsController statisticsController,
                              final HandStrengthEvaluator handStrengthEvaluator,
                              final OpponentModeler opponentModeler,
                              final GameEventDispatcher gameEventDispatcher) {
        this.logger = logger;
        this.handPowerRanker = handPowerRanker;
        this.gameProperties = gameProperties;
        this.statisticsController = statisticsController;
        this.handStrengthEvaluator = handStrengthEvaluator;
        this.opponentModeler = opponentModeler;
        this.gameEventDispatcher = gameEventDispatcher;
    }

    public void play(Game game) {
        logger.log("-----------------------------------------");
        logger.logImportant("Game Hand #" + (game.gameHandsCount() + 1));
        logger.log("-----------------------------------------");

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
        GameHand gameHand = new GameHand(game.getPlayers(), gameProperties, gameEventDispatcher);
        game.addGameHand(gameHand);
        return gameHand;
    }

    protected Boolean playRound(GameHand gameHand) {
        gameHand.nextRound();
        logBettingRound(gameHand);
        int toPlay = gameHand.getPlayersCount();
        if (gameHand.getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            takeBlinds(gameHand);
            toPlay--; // Big blinds don't have to call on himself if no raise :)
        }

        int turn = 1;
        int numberOfPlayersAtBeginningOfRound = gameHand.getPlayersCount();
        while (toPlay > 0) {
            Player player = gameHand.getNextPlayer();
            BettingDecision bettingDecision = player.decide(gameHand);

            // We can't raise at second turn
            if (turn > numberOfPlayersAtBeginningOfRound && bettingDecision.isRaise()) {
                bettingDecision = BettingDecision.call(-1);
            }

            // After a raise, every active players after the raiser must play
            if (bettingDecision.isRaise()) {
                toPlay = gameHand.getPlayersCount() - 1;
            }

            applyDecision(gameHand, player, bettingDecision);
            turn++;
            toPlay--;
        }

        // Check if we have a winner
        if (gameHand.getPlayersCount() == 1) {
            Player winner = gameHand.getCurrentPlayer();
            winner.addMoney(gameHand.getTotalBets());
            logger.log(winner + ": WIN! +" + gameHand.getTotalBets() + "$");
            return true;
        }
        return false;
    }

    private void logBettingRound(GameHand gameHand) {
        String logMsg = "---" + gameHand.getBettingRoundName();
        logMsg += " (" + gameHand.getPlayersCount() + " players, ";
        logMsg += gameHand.getTotalBets() + "$)";
        if (!gameHand.getSharedCards().isEmpty()) {
            logMsg += " " + gameHand.getSharedCards();
        }
        logger.log(logMsg);
    }

    public void takeBlinds(GameHand gameHand) {
        Player smallBlindPlayer = gameHand.getNextPlayer();
        Player bigBlindPlayer = gameHand.getNextPlayer();

        logger.log(smallBlindPlayer + ": Small blind " + gameProperties.getSmallBlind() + "$");
        logger.log(bigBlindPlayer + ": Big blind " + gameProperties.getBigBlind() + "$");

        gameHand.getCurrentBettingRound().placeBet(smallBlindPlayer, gameProperties.getSmallBlind());
        gameHand.getCurrentBettingRound().placeBet(bigBlindPlayer, gameProperties.getBigBlind());
    }

    public void applyDecision(GameHand gameHand, Player player, BettingDecision bettingDecision) {
        double handStrength = handStrengthEvaluator.evaluate(player.getHoleCards(), gameHand.getSharedCards(), gameHand.getPlayersCount());
        gameHand.applyDecision(player, bettingDecision, gameProperties, handStrength);

        BettingRound bettingRound = gameHand.getCurrentBettingRound();
        logger.log(player + ": " + bettingDecision + " " + bettingRound.getBetForPlayer(player) + "$");
    }

    protected List<Player> getWinners(GameHand gameHand) {
        Iterable<Player> activePlayers = gameHand.getPlayers();
        List<Card> sharedCards = gameHand.getSharedCards();

        HandPower bestHandPower = null;
        List<Player> winners = new ArrayList<Player>();
        for (Player player : activePlayers) {
            List<Card> mergeCards = new ArrayList<Card>(player.getHoleCards());
            mergeCards.addAll(sharedCards);
            HandPower handPower = handPowerRanker.rank(mergeCards);

            logger.log(player + ": " + handPower);

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

    protected void showDown(GameHand gameHand) {
        logger.log("--- Showdown");

        // Showdown
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
            logger.log("WINNER: "+winner + ": WIN! +" + gainAndModulo + "$");

            modulo--;
        }

        // Opponent modeling
        opponentModeler.save(gameHand);
    }
}
