package com.larryrun.texasplayer.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.larryrun.texasplayer.controller.ControllerModule;
import com.larryrun.texasplayer.model.gameproperties.AIGameProperties;
import com.larryrun.texasplayer.persistence.PersistenceModule;
import com.larryrun.texasplayer.utils.Logger;

public class AIGameModule extends AbstractModule {
    public AIGameModule() {
    }

    @Override
    protected void configure() {
        install(new ControllerModule());
        install(new PersistenceModule());

        bind(Logger.class).toProvider(LoggerProvider.class).in(Singleton.class);

        bind(AIGameProperties.class).in(Singleton.class);
    }

}
