package com.larryrun.texasplayer.controller.phase1;

import com.larryrun.texasplayer.controller.HandPowerRanker;
import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.model.*;
import com.larryrun.texasplayer.model.cards.Card;

import javax.inject.Inject;
import java.util.List;

public class PlayerControllerPhaseINormal extends PlayerController {
    private final HandPowerRanker handPowerRanker;

    @Inject
    public PlayerControllerPhaseINormal(final HandPowerRanker handPowerRanker) {
        this.handPowerRanker = handPowerRanker;
    }

    @Override
    public String toString() {
        return "PhaseI normal";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand,
                                         List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);

        if (card1.getNumber().equals(card2.getNumber())) {
            return BettingDecision.raise(-1);
        } else if (card1.getNumber().getPower() + card2.getNumber().getPower() > 16
                || canCheck(gameHand, player)) {
            return BettingDecision.call(-1);
        } else {
            return BettingDecision.FOLD;
        }
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        HandPower handPower = handPowerRanker.rank(cards);

        HandPowerType handPowerType = handPower.getHandPowerType();
        if (handPowerType.equals(HandPowerType.HIGH_CARD)) {
            if (canCheck(gameHand, player)) {
                return BettingDecision.call(-1);
            }
            return BettingDecision.FOLD;
        } else if (handPowerType.getPower() >= HandPowerType.STRAIGHT.getPower()) {
            return BettingDecision.raise(-1);
        } else {
            return BettingDecision.call(-1);
        }
    }
}
