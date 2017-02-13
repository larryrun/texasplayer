package com.larryrun.texasplayer.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.larryrun.texasplayer.controller.ControllerModule;
import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.model.event.GameEvent;
import com.larryrun.texasplayer.model.event.GameEventHandler;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.persistence.PersistenceModule;
import com.larryrun.texasplayer.utils.Logger;

import javax.inject.Singleton;

public class TexasModule extends AbstractModule {
    private final LogLevel logLevel;
    private final GamePropertiesParameter gamePropertiesParameter;

    public TexasModule(LogLevel logLevel, GamePropertiesParameter gamePropertiesParameter) {
        this.logLevel = logLevel;
        this.gamePropertiesParameter = gamePropertiesParameter;
    }

    public TexasModule() {
        logLevel = LogLevel.ALL;
        gamePropertiesParameter = GamePropertiesParameter.DEMO;
    }

    @Override
    protected void configure() {
        install(new ControllerModule());
        install(new PersistenceModule());

        bind(LogLevel.class).toInstance(logLevel);
        bind(GamePropertiesParameter.class).toInstance(gamePropertiesParameter);

        bind(GameProperties.class).toProvider(GamePropertiesProvider.class).in(Singleton.class);
        bind(Logger.class).toProvider(LoggerProvider.class).in(Singleton.class);
    }

    @Provides
    public GameEventDispatcher provideGameEventDispatcher() {
        return new GameEventDispatcher(gameEvent -> {});
    }
}
