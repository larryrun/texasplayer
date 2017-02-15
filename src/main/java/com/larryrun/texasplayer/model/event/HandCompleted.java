package com.larryrun.texasplayer.model.event;

import com.google.common.collect.ImmutableList;
import com.larryrun.texasplayer.model.Player;

import java.util.List;

public class HandCompleted extends GameEvent {
    public static final String EVENT_NAME = "HandCompleted";
    private List<Player> winners;
    private boolean showDown;

    public HandCompleted(Player winner, boolean showDown) {
        this.winners = ImmutableList.of(winner);
        this.showDown = showDown;
    }

    public HandCompleted(List<Player> winners, boolean showDown) {
        this.winners = ImmutableList.copyOf(winners);
        this.showDown = showDown;
    }

    public List<Player> getWinners() {
        return winners;
    }

    public boolean isShowDown() {
        return showDown;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
