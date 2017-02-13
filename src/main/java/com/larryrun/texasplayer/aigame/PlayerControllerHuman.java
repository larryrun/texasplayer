package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;

import java.util.List;

public class PlayerControllerHuman extends PlayerController {
    private BettingDecision nextPreFlopDecision;
    private BettingDecision nextAfterFlopDecision;

    @Override
    protected BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        return BettingDecision.call(-1);
    }

    @Override
    protected BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        return BettingDecision.call(-1);
    }

    public BettingDecision getNextPreFlopDecision() {
        return nextPreFlopDecision;
    }

    public void setNextPreFlopDecision(BettingDecision nextPreFlopDecision) {
        this.nextPreFlopDecision = nextPreFlopDecision;
    }

    public BettingDecision getNextAfterFlopDecision() {
        return nextAfterFlopDecision;
    }

    public void setNextAfterFlopDecision(BettingDecision nextAfterFlopDecision) {
        this.nextAfterFlopDecision = nextAfterFlopDecision;
    }
}
