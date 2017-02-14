package com.larryrun.texasplayer.model.event;

import com.google.common.collect.ImmutableList;
import com.larryrun.texasplayer.model.Player;

import java.util.List;

public class HandCompleted extends GameEvent {
    public static final String EVENT_NAME = "HandCompleted";
    private List<Player> winners;

    public HandCompleted(Player winner) {
        this.winners = ImmutableList.of(winner);
    }

    public HandCompleted(List<Player> winners) {
        this.winners = ImmutableList.copyOf(winners);
    }

    public List<Player> getWinners() {
        return winners;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
