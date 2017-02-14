package com.larryrun.texasplayer.controller.phase2;

import com.larryrun.texasplayer.controller.EquivalenceClassController;
import com.larryrun.texasplayer.controller.HandStrengthEvaluator;
import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.cards.EquivalenceClass;
import com.larryrun.texasplayer.persistence.PreFlopPersistence;

import javax.inject.Inject;
import java.util.List;

public class PlayerControllerPhaseIINormal extends PlayerControllerPhaseII {
    private final EquivalenceClassController equivalenceClassController;
    private final PreFlopPersistence preFlopPersistence;

    @Inject
    public PlayerControllerPhaseIINormal(final EquivalenceClassController equivalenceClassController,
                                         final PreFlopPersistence preFlopPersistence,
                                         final HandStrengthEvaluator handStrengthEvaluator) {
        super(handStrengthEvaluator);

        this.equivalenceClassController = equivalenceClassController;
        this.preFlopPersistence = preFlopPersistence;
    }

    @Override
    public String toString() {
        return "PhaseII normal";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        EquivalenceClass equivalenceClass = this.equivalenceClassController.cards2Equivalence(card1, card2);
        double percentageOfWins = preFlopPersistence.retrieve(gameHand.getPlayers().size(), equivalenceClass);

        if (percentageOfWins > 0.6)
            return BettingDecision.raise(-1);
        else if (percentageOfWins < 0.45)
            return BettingDecision.FOLD;
        return BettingDecision.call(-1);
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        double p = calculateCoefficient(gameHand, player);

        if (p > 0.8) {
            return BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + gameHand.getGameProperties().getBigBlind());
        } else if (p > 0.4 || canCheck(gameHand, player)) {
            return BettingDecision.call(gameHand.getCurrentBettingRound().getHighestBet());
        }
        return BettingDecision.FOLD;
    }
}
