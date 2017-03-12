package com.larryrun.texasplayer.model;

import com.larryrun.texasplayer.aigame.PlayerControllerHuman;
import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.controller.PlayerController;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.event.PlayerBalanceChanged;

import java.util.Arrays;
import java.util.List;

public class Player {
    private final int number;
    private String name;
    private final PlayerController playerController;
    private int money;
    private List<Card> holeCards;
    private GameEventDispatcher gameEventDispatcher;

    public Player(String name, int number, int initialMoney, PlayerController playerController, GameEventDispatcher gameEventDispatcher) {
        this.name = name;
        this.number = number;
        this.money = initialMoney;
        this.playerController = playerController;
        this.gameEventDispatcher = gameEventDispatcher;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }

        Player otherPlayer = (Player) o;

        return name.equals(otherPlayer.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Player #");
        stringBuilder.append(getNumber());

        if (holeCards != null) {
            stringBuilder.append(holeCards.toString());
        }

        return stringBuilder.toString();
    }

    public BettingDecision decide(GameHand gameHand) {
        return playerController.decide(this, gameHand);
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getMoney() {
        return money;
    }

    public void removeMoney(int amount) {
        money -= amount;
        this.gameEventDispatcher.fireEvent(new PlayerBalanceChanged(this, money));
    }

    public void addMoney(int amount) {
        money += amount;
        this.gameEventDispatcher.fireEvent(new PlayerBalanceChanged(this, money));
    }

    public void setHoleCards(Card hole1, Card hole2) {
        holeCards = Arrays.asList(hole1, hole2);
    }

    public List<Card> getHoleCards() {
        return holeCards;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public boolean isHumanPlayer() {
        return playerController instanceof PlayerControllerHuman;
    }
}
