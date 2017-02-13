package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.Player;

public class PlayerCreated extends GameEvent {
    public static final String EVENT_NAME = "PlayerCreated";

    private Player player;
    public PlayerCreated(Player player) {
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
