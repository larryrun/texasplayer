package com.larryrun.texasplayer.controller.phase3;

import com.larryrun.texasplayer.controller.HandStrengthEvaluator;
import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIINormal;
import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.Player;

import javax.inject.Inject;

public class PlayerControllerPhaseIIIAgressive extends PlayerControllerPhaseIII {
    @Inject
    public PlayerControllerPhaseIIIAgressive(PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                                                HandStrengthEvaluator handStrengthEvaluator,
                                                OpponentModeler opponentModeler) {
        super(playerControllerPhaseIINormal, handStrengthEvaluator, opponentModeler);
    }

    @Override
    public String toString() {
        return "PhaseIII Agressive";
    }

    @Override
    protected BettingDecision decideBet(GameHand gameHand, Player player,
                                        int oppponentsWithBetterEstimatedHandStrength,
                                        int opponentsModeledCount) {
        if ((double) oppponentsWithBetterEstimatedHandStrength / opponentsModeledCount > 0.5) {
            return BettingDecision.raise(-1);
        } else if (canCheck(gameHand, player)) {
            return BettingDecision.CALL;
        } else {
            return BettingDecision.FOLD;
        }
    }
}
