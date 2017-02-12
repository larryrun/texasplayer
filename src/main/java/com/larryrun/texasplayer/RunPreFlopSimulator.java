package com.larryrun.texasplayer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.larryrun.texasplayer.controller.preflopsim.PreFlopSimulatorController;
import com.larryrun.texasplayer.dependencyinjection.GamePropertiesParameter;
import com.larryrun.texasplayer.dependencyinjection.LogLevel;
import com.larryrun.texasplayer.dependencyinjection.TexasModule;

public class RunPreFlopSimulator {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new TexasModule(LogLevel.IMPORTANT, GamePropertiesParameter.PHASE1));

        PreFlopSimulatorController preFlopSimulatorController = injector
                .getInstance(PreFlopSimulatorController.class);
        preFlopSimulatorController.play();
    }
}
