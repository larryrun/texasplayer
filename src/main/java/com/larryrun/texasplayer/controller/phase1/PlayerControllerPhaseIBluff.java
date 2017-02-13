package com.larryrun.texasplayer.controller.phase1;

import com.larryrun.texasplayer.controller.HandPowerRanker;
import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.model.*;
import com.larryrun.texasplayer.model.cards.Card;

import javax.inject.Inject;
import java.util.List;

public class PlayerControllerPhaseIBluff extends PlayerController {
    private final HandPowerRanker handPowerRanker;

    @Inject
    public PlayerControllerPhaseIBluff(final HandPowerRanker handPowerRanker) {
        this.handPowerRanker = handPowerRanker;
    }

    @Override
    public String toString() {
        return "PhaseI bluff";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand,
                                         List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        int sumPower = card1.getNumber().getPower() + card2.getNumber().getPower();

        if (card1.getNumber().equals(card2.getNumber()) || sumPower <= 8) {
            return BettingDecision.raise(-1);
        } else {
            if (sumPower > 16 || canCheck(gameHand, player)) {
                return BettingDecision.call(-1);
            } else {
                return BettingDecision.FOLD;
            }
        }
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand,
                                           List<Card> cards) {
        HandPower handPower = handPowerRanker.rank(cards);

        HandPowerType handPowerType = handPower.getHandPowerType();
        if (handPowerType.equals(HandPowerType.HIGH_CARD)) {
            return BettingDecision.raise(-1);
        } else if (handPowerType.getPower() >= HandPowerType.STRAIGHT.getPower()) {
            return BettingDecision.raise(-1);
        } else {
            if(canCheck(gameHand, player)){
                return BettingDecision.call(-1);
            }
            return BettingDecision.FOLD;
        }
    }
}
