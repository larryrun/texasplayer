package com.larryrun.texasplayer.dependencyinjection;

import com.larryrun.texasplayer.model.gameproperties.*;

import javax.inject.Inject;
import javax.inject.Provider;

public class GamePropertiesProvider implements Provider<GameProperties> {
    private final GamePropertiesParameter gamePropertiesParameter;
    private final DemoGameProperties demoGameProperties;

    @Inject
    public GamePropertiesProvider(GamePropertiesParameter gamePropertiesParameter,
                                  DemoGameProperties demoGameProperties) {
        this.gamePropertiesParameter = gamePropertiesParameter;
        this.demoGameProperties = demoGameProperties;
    }

    @Override
    public GameProperties get() {
        switch (gamePropertiesParameter){
            default:
                return demoGameProperties;
        }
    }
}
