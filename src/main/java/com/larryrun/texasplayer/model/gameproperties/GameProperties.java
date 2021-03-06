package com.larryrun.texasplayer.model.gameproperties;

import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.event.PlayerCreated;

import java.util.ArrayList;
import java.util.List;

public abstract class GameProperties {
    private final int smallBlind;
    private final int bigBlind;
    private final int initialMoney;
    private final int numberOfHands;
    private final List<Player> players = new ArrayList<Player>();
    private final GameEventDispatcher gameEventDispatcher;

    protected GameProperties(int numberOfHands, int initialMoney, int bigBlind, int smallBlind, GameEventDispatcher gameEventDispatcher) {
        this.numberOfHands = numberOfHands;
        this.initialMoney = initialMoney;
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;
        this.gameEventDispatcher = gameEventDispatcher;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public int getNumberOfHands() {
        return numberOfHands;
    }

    public List<Player> getPlayers() {
        return players;
    }

    protected void addPlayer(Player player){
        players.add(player);
    }
}
