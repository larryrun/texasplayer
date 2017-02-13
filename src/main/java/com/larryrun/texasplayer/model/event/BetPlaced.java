package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.BettingDecision;
import com.larryrun.texasplayer.model.Player;

public class BetPlaced extends GameEvent {
    public static final String EVENT_NAME = "BetPlaced";
    private Player player;
    private BettingDecision bettingDecision;

    public BetPlaced(Player player, BettingDecision bettingDecision) {
        this.player = player;
        this.bettingDecision = bettingDecision;
    }

    public Player getPlayer() {
        return player;
    }

    public BettingDecision getBettingDecision() {
        return bettingDecision;
    }

    @Override
    public String eventName() {
        return null;
    }
}
