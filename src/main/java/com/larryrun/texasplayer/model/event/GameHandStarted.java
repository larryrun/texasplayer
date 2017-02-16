package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.Player;

public class GameHandStarted extends GameEvent {
    public static final String EVENT_NAME = "GameHandStarted";
    private Player dealer, sb, bb;

    public GameHandStarted(Player dealer, Player sb, Player bb) {
        this.dealer = dealer;
        this.sb = sb;
        this.bb = bb;
    }

    public Player getDealer() {
        return dealer;
    }

    public Player getBbPlayer() {
        return bb;
    }

    public Player getSbPlayer() {
        return sb;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
