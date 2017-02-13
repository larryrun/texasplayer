package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.Player;

public class SBTaken extends GameEvent {
    public static final String EVENT_NAME = "SBTaken";
    private Player player;

    public SBTaken(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
