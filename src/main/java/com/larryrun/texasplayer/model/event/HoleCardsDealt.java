package com.larryrun.texasplayer.model.event;

import com.google.common.collect.ImmutableList;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;

import java.util.List;

public class HoleCardsDealt extends GameEvent {
    public static final String EVENT_NAME = "HoleCardsDealt";
    private Player player;
    private List<Card> cards;

    public HoleCardsDealt(Player player, Card card1, Card card2) {
        this.player = player;
        cards = ImmutableList.of(card1, card2);
    }

    public Player getPlayer() {
        return player;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
