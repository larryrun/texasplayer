package com.larryrun.texasplayer.model.event;

import com.google.common.collect.ImmutableList;
import com.larryrun.texasplayer.model.cards.Card;

import java.util.List;

public class FlopCardsDealt extends GameEvent {
    public static final String EVENT_NAME = "FlopCardsDealt";
    private List<Card> cards;

    public FlopCardsDealt(Card card1, Card card2, Card card3) {
        cards = ImmutableList.of(card1, card2, card3);
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
