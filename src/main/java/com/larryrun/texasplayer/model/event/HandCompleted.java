package com.larryrun.texasplayer.model.event;

import com.google.common.collect.ImmutableList;
import com.larryrun.texasplayer.model.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HandCompleted extends GameEvent {
    public static final String EVENT_NAME = "HandCompleted";
    private List<Player> winners;
    private List<Player> showdownPlayers;

    public HandCompleted(List<Player> winners, Collection<Player> showdownPlayers) {
        this.winners = ImmutableList.copyOf(winners);
        if(showdownPlayers == null) {
            showdownPlayers = Collections.emptyList();
        }
        this.showdownPlayers = ImmutableList.copyOf(showdownPlayers);
    }

    public List<Player> getWinners() {
        return winners;
    }

    public List<Player> getShowdownPlayers() {
        return showdownPlayers;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
