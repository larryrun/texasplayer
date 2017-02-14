package com.larryrun.texasplayer.controller.preflopsim;

import com.larryrun.texasplayer.controller.HandPowerRanker;
import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.model.*;
import com.larryrun.texasplayer.model.cards.Card;

import javax.inject.Inject;
import java.util.List;

/**
 * A naive player that cannot fold but only bet. Used during pre flop rollout
 * simulations
 */
public class PlayerControllerPreFlopRoll extends PlayerController {
    private final HandPowerRanker handPowerRanker;

    @Inject
    public PlayerControllerPreFlopRoll(final HandPowerRanker handPowerRanker) {
        this.handPowerRanker = handPowerRanker;
    }

    @Override
    public String toString() {
        return "Preflop";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand,
                                         List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);

        if (card1.getNumber().equals(card2.getNumber())) {
            return BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + gameHand.getGameProperties().getBigBlind());
        } else if (card1.getNumber().getPower() + card2.getNumber().getPower() > 16 || canCheck(gameHand, player)) {
            return BettingDecision.call(gameHand.getCurrentBettingRound().getHighestBet());
        } else {
            return BettingDecision.call(gameHand.getCurrentBettingRound().getHighestBet());
        }
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        HandPower handPower = handPowerRanker.rank(cards);

        HandPowerType handPowerType = handPower.getHandPowerType();
        if (handPowerType.equals(HandPowerType.HIGH_CARD)) {
            if (canCheck(gameHand, player)) {
                return BettingDecision.call(gameHand.getCurrentBettingRound().getHighestBet());
            }
            return BettingDecision.call(gameHand.getCurrentBettingRound().getHighestBet());
        } else if (handPowerType.getPower() >= HandPowerType.THREE_OF_A_KIND.getPower()) {
            return BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + gameHand.getGameProperties().getBigBlind());
        } else {
            return BettingDecision.call(gameHand.getCurrentBettingRound().getHighestBet());
        }
    }
}
