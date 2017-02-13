package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.HandStrengthEvaluator;
import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIINormal;
import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.BettingRound;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.model.opponentmodeling.ContextAction;
import com.larryrun.texasplayer.model.opponentmodeling.ModelResult;

import javax.inject.Inject;
import java.util.List;

public class PlayerControllerAI extends PlayerController {
    private final PlayerControllerPhaseIINormal playerControllerPhaseIINormal;
    private final HandStrengthEvaluator handStrengthEvaluator;
    private final OpponentModeler opponentModeler;

    @Inject
    public PlayerControllerAI(PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                              HandStrengthEvaluator handStrengthEvaluator,
                              OpponentModeler opponentModeler) {
        this.playerControllerPhaseIINormal = playerControllerPhaseIINormal;
        this.handStrengthEvaluator = handStrengthEvaluator;
        this.opponentModeler = opponentModeler;
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        return playerControllerPhaseIINormal.decidePreFlop(player, gameHand, cards);
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
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
            return playerControllerPhaseIINormal.decideAfterFlop(player, gameHand, cards);
        }

        return decideBet(gameHand, player, opponentsWithBetterEstimatedHandStrength, opponentsModeledCount);
    }

    private BettingDecision decideBet(GameHand gameHand, Player player,
                                        int opponentsWithBetterEstimatedHandStrength,
                                        int opponentsModeledCount) {
        if ((double) opponentsWithBetterEstimatedHandStrength / opponentsModeledCount > 0.5) {
            return BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + gameHand.getGameProperties().getBigBlind());
        } else if (canCheck(gameHand, player)) {
            return BettingDecision.call(gameHand.getCurrentBettingRound().getHighestBet());
        } else {
            return BettingDecision.FOLD;
        }
    }
}
