package com.larryrun.texasplayer.controller;

import com.google.inject.AbstractModule;
import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.controller.phase1.PlayerControllerPhaseIBluff;
import com.larryrun.texasplayer.controller.phase1.PlayerControllerPhaseINormal;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIIBluff;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIINormal;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIAgressive;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIConservative;
import com.larryrun.texasplayer.controller.player.PlayerControllerSelf;
import com.larryrun.texasplayer.controller.preflopsim.PreFlopSimulatorModule;

import javax.inject.Singleton;

public class ControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new PreFlopSimulatorModule());

        bind(GameHandController.class).in(Singleton.class);
        bind(HandPowerRanker.class).in(Singleton.class);
        bind(StatisticsController.class).in(Singleton.class);
        bind(HandStrengthEvaluator.class).in(Singleton.class);
        bind(EquivalenceClassController.class).in(Singleton.class);
        bind(OpponentModeler.class).in(Singleton.class);

        bind(PlayerControllerPhaseINormal.class).in(Singleton.class);
        bind(PlayerControllerPhaseIBluff.class).in(Singleton.class);
        bind(PlayerControllerPhaseIINormal.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIBluff.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIIAgressive.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIIConservative.class).in(Singleton.class);

        bind(PlayerControllerSelf.class).in(Singleton.class);
    }
}
