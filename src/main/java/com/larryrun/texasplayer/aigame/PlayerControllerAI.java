package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.EquivalenceClassController;
import com.larryrun.texasplayer.controller.HandStrengthEvaluator;
import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.model.*;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.cards.EquivalenceClass;
import com.larryrun.texasplayer.model.opponentmodeling.ContextAction;
import com.larryrun.texasplayer.model.opponentmodeling.ContextRaises;
import com.larryrun.texasplayer.model.opponentmodeling.ModelResult;
import com.larryrun.texasplayer.persistence.PreFlopPersistence;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerControllerAI extends PlayerController {
    private final HandStrengthEvaluator handStrengthEvaluator;
    private final OpponentModeler opponentModeler;
    private final EquivalenceClassController equivalenceClassController;
    private final PreFlopPersistence preFlopPersistence;


    @Inject
    public PlayerControllerAI(
            final EquivalenceClassController equivalenceClassController,
            final PreFlopPersistence preFlopPersistence,
            final HandStrengthEvaluator handStrengthEvaluator,
            final OpponentModeler opponentModeler) {
        this.handStrengthEvaluator = handStrengthEvaluator;
        this.opponentModeler = opponentModeler;
        this.equivalenceClassController = equivalenceClassController;
        this.preFlopPersistence = preFlopPersistence;
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        }catch (InterruptedException e){}

        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        EquivalenceClass equivalenceClass = this.equivalenceClassController.cards2Equivalence(card1, card2);
        double percentageOfWins = preFlopPersistence.retrieve(gameHand.getPlayers().size(), equivalenceClass);

        if (percentageOfWins > 0.6)
            return BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + gameHand.getGameProperties().getBigBlind());
        else if (percentageOfWins < 0.45)
            return BettingDecision.FOLD;

        return BettingDecision.CALL;
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        }catch (InterruptedException e){}

        BettingRound currentBettingRound = gameHand.getCurrentBettingRound();
        double handStrength = handStrengthEvaluator.evaluate(player.getHoleCards(), gameHand.getSharedCards(), gameHand.getPlayersCount());
        int opponentsModeledCount = 0;
        int opponentsWithBetterEstimatedHandStrength = 0;

        for (Player opponent : gameHand.getPlayers()) {
            // Only try to model opponent
            if (!opponent.equals(player)) {
                ContextAction contextAction = currentBettingRound.getContextActionForPlayer(opponent);

                if (contextAction != null) {
                    ModelResult modelResult = opponentModeler.getEstimatedHandStrength(contextAction);

                    // If we don't have enough occurence or if the variance is big, the information is not valuable
                    if (modelResult.getNumberOfOccurences() > 10 && modelResult.getHandStrengthDeviation() <= 0.15) {
                        opponentsModeledCount++;
                        if (modelResult.getHandStrengthAverage() > handStrength) {
                            opponentsWithBetterEstimatedHandStrength++;
                        }
                    }
                }
            }
        }

        // If we don't have enough context action in the current betting round
        if ((double) opponentsModeledCount / gameHand.getPlayersCount() < 0.5) {
            // We fallback to a phase II bot

            double p = calculateCoefficient(gameHand, player);

            if (p > 0.8) {
                return BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + gameHand.getGameProperties().getBigBlind());
            } else if (p > 0.4 || canCheck(gameHand, player)) {
                return BettingDecision.CALL;
            }
            return BettingDecision.FOLD;
        }

        return decideBet(gameHand, player, opponentsWithBetterEstimatedHandStrength, opponentsModeledCount);
    }

    private BettingDecision decideBet(GameHand gameHand, Player player,
                                        int opponentsWithBetterEstimatedHandStrength,
                                        int opponentsModeledCount) {
        if ((double) opponentsWithBetterEstimatedHandStrength / opponentsModeledCount > 0.5) {
            return BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + gameHand.getGameProperties().getBigBlind());
        } else if (canCheck(gameHand, player)) {
            return BettingDecision.CALL;
        } else {
            return BettingDecision.FOLD;
        }
    }

    private double calculateCoefficient(GameHand gameHand, Player player) {
        double p = this.handStrengthEvaluator.evaluate(player.getHoleCards(), gameHand.getSharedCards(), gameHand.getPlayers().size());

        // Decision must depends on the number of players
        p = p * (1 + gameHand.getPlayersCount() / 20);

        // Last round, why not?
        if (gameHand.getBettingRoundName().equals(BettingRoundName.POST_RIVER)) {
            p += 0.3;
        }
        // Lot of raises, be careful
        if (ContextRaises.valueFor(gameHand.getCurrentBettingRound().getNumberOfRaises()).equals(ContextRaises.MANY)) {
            p -= 0.3;
        }

        return p;
    }
}
