package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.utils.AssertUtils;

import java.util.List;

public class PlayerControllerHuman extends PlayerController {
    private volatile BettingDecision nextDecision;
    private volatile int bbMultiplyTo = 1;

    @Override
    protected BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        AssertUtils.notNull(nextDecision, "nextDecision");

        decideAmount(player, gameHand);
        return nextDecision;
    }

    @Override
    protected BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        AssertUtils.notNull(nextDecision, "nextDecision");

        decideAmount(player, gameHand);
        return nextDecision;
    }

    public void setNextDecision(BettingDecision nextDecision) {
        this.nextDecision = nextDecision;
    }

    public void setNextDecisionToFold() {
        this.nextDecision = BettingDecision.FOLD;
    }

    public void setNextDecisionToCall() {
        this.nextDecision = BettingDecision.CALL;
    }

    public void setNextDecisionToRaise3BB() {
        this.nextDecision = BettingDecision.raise(-1);
        bbMultiplyTo = 3;
    }

    public void setNextDecisionToRaise2BB() {
        this.nextDecision = BettingDecision.raise(-1);
        bbMultiplyTo = 2;
    }

    public void setNextDecisionToRaiseBB() {
        this.nextDecision = BettingDecision.raise(-1);
        bbMultiplyTo = 1;
    }

    private void decideAmount(Player player, GameHand gameHand) {
        if(nextDecision.isCall()) {
            nextDecision = BettingDecision.CALL;
        }else if(nextDecision.isRaise()) {
            if(nextDecision.getAmount() > 0) {
                nextDecision = BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + nextDecision.getAmount());
            }else {
                nextDecision = BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + gameHand.getGameProperties().getBigBlind() * bbMultiplyTo);
            }
        }
        bbMultiplyTo = 1;
    }

}
