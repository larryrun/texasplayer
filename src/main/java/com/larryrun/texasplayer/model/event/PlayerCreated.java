package com.larryrun.texasplayer.model.event;

import com.google.common.collect.ImmutableList;
import com.larryrun.texasplayer.model.Player;

import java.util.List;

public class PlayerCreated extends GameEvent {
    public static final String EVENT_NAME = "PlayerCreated";

    private List<Player> players;
    public PlayerCreated(List<Player> players) {
        this.players = ImmutableList.copyOf(players);
    }

    public List<Player> getPlayer() {
        return players;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
