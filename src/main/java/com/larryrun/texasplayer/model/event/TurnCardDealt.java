package com.larryrun.texasplayer.model.event;

import com.larryrun.texasplayer.model.cards.Card;

public class TurnCardDealt extends GameEvent {
    public static final String EVENT_NAME = "TurnCardDealt";
    private Card card;

    public TurnCardDealt(Card card) {
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
