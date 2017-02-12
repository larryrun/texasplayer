package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;

import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class PlayerControllerHuman extends PlayerController {
    private SynchronousQueue<BettingDecision> preFlopBettingDecisionQueue;
    private SynchronousQueue<BettingDecision> nextAfterFlopBettingDecisionQueue;

    @Override
    protected BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        try {
            return preFlopBettingDecisionQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        try {
            return nextAfterFlopBettingDecisionQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPreFlopBettingDecision(BettingDecision bettingDecision) {
        preFlopBettingDecisionQueue.offer(bettingDecision);
    }

    public void setNextAfterFlopBettingDecision(BettingDecision bettingDecision) {
        nextAfterFlopBettingDecisionQueue.offer(bettingDecision);
    }

}
