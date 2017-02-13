package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.Player;

public class PlayerOffTable extends GameEvent {
    public static final String EVENT_NAME = "PlayerOffTable";

    private Player player;
    public PlayerOffTable(Player player) {
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
