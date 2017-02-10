package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.GameHandController;
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
    private final HandPowerRanker handPowerRanker;
    private final GameProperties gameProperties;
    private final StatisticsController statisticsController;
    private final HandStrengthEvaluator handStrengthEvaluator;
    private final OpponentModeler opponentModeler;

    @Inject
    public AIGameGameHandController(
            final HandPowerRanker handPowerRanker,
            final GameProperties gameProperties,
            final StatisticsController statisticsController,
            final HandStrengthEvaluator handStrengthEvaluator,
            final OpponentModeler opponentModeler) {

        this.handPowerRanker = handPowerRanker;
        this.gameProperties = gameProperties;
        this.statisticsController = statisticsController;
        this.handStrengthEvaluator = handStrengthEvaluator;
        this.opponentModeler = opponentModeler;
    }

    public void play(Game game) {
        GameHand gameHand = createGameHand(game);

        Boolean haveWinner = false;
        while (!gameHand.getBettingRoundName().equals(BettingRoundName.POST_RIVER) && !haveWinner) {
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

    private GameRound currentGameRound;

    protected Boolean playRound(GameHand gameHand) {
        currentGameRound = new GameRound(gameHand, gameProperties, handStrengthEvaluator);

        gameHand.nextRound();
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
            if (turn > numberOfPlayersAtBeginningOfRound
                    && bettingDecision.equals(BettingDecision.RAISE)) {
                bettingDecision = BettingDecision.CALL;
            }

            // After a raise, every active players after the raiser must play
            if (bettingDecision.equals(BettingDecision.RAISE)) {
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
            return true;
        }
        return false;
    }

    public void playAIPlayersMove() {

    }


    protected List<Player> getWinners(GameHand gameHand) {
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

    protected void showDown(GameHand gameHand) {
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
