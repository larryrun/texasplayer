package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.Player;

public class PlayerBalanceChanged extends GameEvent {
    public static final String EVENT_NAME = "PlayerBalanceChanged";
    private Player player;
    private int amount;

    public PlayerBalanceChanged(Player player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    public Player getPlayer() {
        return player;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
