package com.larryrun.texasplayer.aigame;

import com.google.inject.AbstractModule;
import com.larryrun.texasplayer.controller.*;
import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIAgressive;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIConservative;
import com.larryrun.texasplayer.controller.preflopsim.PreFlopSimulatorModule;

import javax.inject.Singleton;

public class AIGameControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new PreFlopSimulatorModule());

        bind(GameHandController.class).in(Singleton.class);
        bind(HandPowerRanker.class).in(Singleton.class);
        bind(StatisticsController.class).in(Singleton.class);
        bind(HandStrengthEvaluator.class).in(Singleton.class);
        bind(EquivalenceClassController.class).in(Singleton.class);
        bind(OpponentModeler.class).in(Singleton.class);

        bind(PlayerControllerPhaseIIIAgressive.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIIConservative.class).in(Singleton.class);

        bind(PlayerControllerHuman.class).in(Singleton.class);
    }
}
