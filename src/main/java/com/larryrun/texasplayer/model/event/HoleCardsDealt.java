package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;

public class HoleCardsDealt extends GameEvent {
    private Player player;
    private Card card1, card2;

    public HoleCardsDealt(Player player, Card card1, Card card2) {
        this.player = player;
        this.card1 = card1;
        this.card2 = card2;
    }

    public Player getPlayer() {
        return player;
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
        return card2;
    }
}
