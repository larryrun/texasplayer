package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;

import java.util.List;

public class PlayerControllerSelf extends PlayerController {
    private BettingDecision preFlopBettingDecision;
    private BettingDecision nextAfterFlopBettingDecision;

    @Override
    protected BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        return preFlopBettingDecision;
    }

    @Override
    protected BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        return nextAfterFlopBettingDecision;
    }

    public void setPreFlopBettingDecision(BettingDecision bettingDecision) {
        this.preFlopBettingDecision = bettingDecision;
    }

    public void setNextAfterFlopBettingDecision(BettingDecision bettingDecision) {
        this.nextAfterFlopBettingDecision = bettingDecision;
    }

}
