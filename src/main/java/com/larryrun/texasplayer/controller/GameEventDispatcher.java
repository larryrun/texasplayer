package com.larryrun.texasplayer.controller;

import com.larryrun.texasplayer.model.event.GameEvent;
import com.larryrun.texasplayer.model.event.GameEventHandler;

public class GameEventDispatcher {
    private GameEventHandler eventHandler;
    public GameEventDispatcher(GameEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void fireEvent(GameEvent gameEvent) {
        eventHandler.handleGameEvent(gameEvent);
    }
}
