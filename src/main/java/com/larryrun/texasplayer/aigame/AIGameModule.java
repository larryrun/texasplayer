package com.larryrun.texasplayer.aigame;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.dependencyinjection.LogLevel;
import com.larryrun.texasplayer.dependencyinjection.LoggerProvider;
import com.larryrun.texasplayer.model.event.GameEventHandler;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.persistence.PersistenceModule;
import com.larryrun.texasplayer.utils.Logger;

public class AIGameModule extends AbstractModule {
    private GameEventHandler gameEventHandler;
    public AIGameModule(GameEventHandler gameEventHandler) {
        this.gameEventHandler = gameEventHandler;
    }

    @Override
    protected void configure() {
        install(new AIGameControllerModule());
        install(new PersistenceModule());

        bind(LogLevel.class).toInstance(LogLevel.ALL);
        bind(Logger.class).toProvider(LoggerProvider.class).in(Singleton.class);

        bind(GameProperties.class).to(AIGameProperties.class).in(Singleton.class);
    }

    @Provides
    public GameEventDispatcher provideGameEventDispatcher() {
        return new GameEventDispatcher(gameEventHandler);
    }

}
