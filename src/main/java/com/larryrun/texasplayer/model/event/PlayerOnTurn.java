package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.Player;

public class PlayerOnTurn extends GameEvent {
    public static final String EVENT_NAME = "PlayerOnTurn";

    private Player player;
    public PlayerOnTurn(Player player) {
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
