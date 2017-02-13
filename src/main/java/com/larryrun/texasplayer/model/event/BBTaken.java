package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.Player;

public class BBTaken extends GameEvent {
    public static final String EVENT_NAME = "BBTaken";
    private Player player;

    public BBTaken(Player player) {
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
