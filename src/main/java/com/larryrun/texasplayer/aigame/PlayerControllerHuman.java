package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.utils.AssertUtils;

import java.util.List;

public class PlayerControllerHuman extends PlayerController {
    private BettingDecision nextDecision;

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

    private void decideAmount(Player player, GameHand gameHand) {
        if(nextDecision.isCall()) {
            int callAmount = gameHand.getCurrentBettingRound().getHighestBet() - gameHand.getCurrentBettingRound().getBetForPlayer(player);
            nextDecision = BettingDecision.CALL;
        }else if(nextDecision.isRaise()) {
            nextDecision = BettingDecision.raise(gameHand.getCurrentBettingRound().getHighestBet() + gameHand.getGameProperties().getBigBlind());
        }
    }

}
