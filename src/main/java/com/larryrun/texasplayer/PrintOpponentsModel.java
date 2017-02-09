package com.larryrun.texasplayer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.larryrun.texasplayer.dependencyinjection.LogLevel;
import com.larryrun.texasplayer.dependencyinjection.TexasModule;
import com.larryrun.texasplayer.persistence.OpponentsModelPersistence;

public class PrintOpponentsModel {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.DEMO));

        OpponentsModelPersistence opponentsModelPersistence = injector.getInstance(OpponentsModelPersistence.class);
        opponentsModelPersistence.print();
    }
}
