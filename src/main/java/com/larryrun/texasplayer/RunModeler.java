package com.larryrun.texasplayer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.larryrun.texasplayer.controller.PokerController;
import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.dependencyinjection.LogLevel;
import com.larryrun.texasplayer.dependencyinjection.TexasModule;
import com.larryrun.texasplayer.model.opponentmodeling.ContextAggregate;
import com.larryrun.texasplayer.persistence.OpponentsModelPersistence;

import java.util.List;

public class RunModeler {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new TexasModule(LogLevel.IMPORTANT, GamePropertiesParameter.PHASE3));
        OpponentsModelPersistence opponentsModelPersistence = injector.getInstance(OpponentsModelPersistence.class);
        opponentsModelPersistence.clear();

        PokerController pokerController = injector.getInstance(PokerController.class);
        pokerController.play();

        OpponentModeler opponentModeler = injector.getInstance(OpponentModeler.class);
        persistOpponentModelingData(opponentModeler, opponentsModelPersistence);
    }

    private static void persistOpponentModelingData(OpponentModeler opponentModeler, OpponentsModelPersistence
            opponentsModelPersistence) {
        for (List<ContextAggregate> playerModel : opponentModeler.getPlayerModels().values()) {
            for (ContextAggregate contextAggregate : playerModel) {
                opponentsModelPersistence.persist(contextAggregate);
            }
        }
    }
}
