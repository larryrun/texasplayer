package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.cards.Card;

public class RiverCardDealt extends GameEvent {
    public static final String EVENT_NAME = "RiverCardDealt";
    private Card card;

    public RiverCardDealt(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    @Override
    public String eventName() {
        return EVENT_NAME;
    }
}
