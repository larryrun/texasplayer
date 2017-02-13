package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.Player;

public class GameHandStarted extends GameEvent {
    public static final String EVENT_NAME = "GameHandStarted";
    private Player dealer;

    public GameHandStarted(Player dealer) {
        this.dealer = dealer;
    }

    public Player getDealer() {
        return dealer;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
