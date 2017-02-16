package com.larryrun.texasplayer.controller.preflopsim;

import com.larryrun.texasplayer.controller.EquivalenceClassController;
import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.controller.StatisticsController;
import com.larryrun.texasplayer.model.Game;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.EquivalenceClass;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.persistence.PreFlopPersistence;
import com.larryrun.texasplayer.utils.Logger;

import javax.inject.Inject;
import java.util.Collection;

public class PreFlopSimulatorController {
    private static final int ROLLOUTS_PER_EQUIV_CLASS = 100;

    private final Game game = new Game();
    private final Logger logger;
    private final GameProperties gameProperties;
    private final PlayerControllerPreFlopRoll playerControllerPreFlopRoll;
    private final EquivalenceClassController equivalenceClassController;
    private final GameHandControllerPreFlopRoll gameHandControllerPreFlopRoll;
    private final StatisticsController statisticsController;
    private final PreFlopPersistence preFlopPersistence;
    private final GameEventDispatcher gameEventDispatcher;

    @Inject
    public PreFlopSimulatorController(final Logger logger, final GameProperties gameProperties,
                                      final PlayerControllerPreFlopRoll playerControllerPreFlopRoll,
                                      final EquivalenceClassController equivalenceClassController,
                                      final GameHandControllerPreFlopRoll gameHandControllerPreFlopRoll,
                                      final StatisticsController statisticsController,
                                      final PreFlopPersistence preFlopPersistence,
                                      final GameEventDispatcher gameEventDispatcher) {
        this.logger = logger;
        this.gameProperties = gameProperties;
        this.playerControllerPreFlopRoll = playerControllerPreFlopRoll;
        this.equivalenceClassController = equivalenceClassController;
        this.gameHandControllerPreFlopRoll = gameHandControllerPreFlopRoll;
        this.statisticsController = statisticsController;
        this.preFlopPersistence = preFlopPersistence;
        this.gameEventDispatcher = gameEventDispatcher;
    }

    public void play() {
        this.equivalenceClassController.generateAllEquivalenceClass();

        game.addPlayer(new Player("player1", 1, gameProperties.getInitialMoney(), playerControllerPreFlopRoll, gameEventDispatcher));
        Collection<EquivalenceClass> equivalenceClasses = equivalenceClassController.getEquivalenceClasses();

        for (int numberOfPlayers = 2; numberOfPlayers <= 10; numberOfPlayers++) {
            game.addPlayer(new Player("player2", numberOfPlayers, 0, playerControllerPreFlopRoll, gameEventDispatcher));

            for (EquivalenceClass equivalenceClass : equivalenceClasses) {
                statisticsController.initializeStatistics();

                for (int i = 0; i < ROLLOUTS_PER_EQUIV_CLASS; i++) {
                    gameHandControllerPreFlopRoll.play(game, equivalenceClass);
                    game.setNextDealer();
                }

                double percentageWin = (double) statisticsController.getPlayer1Wins() / ROLLOUTS_PER_EQUIV_CLASS;
                preFlopPersistence.persist(numberOfPlayers, equivalenceClass, percentageWin);

                logger.logImportant("=================");
                logger.logImportant("STATISTICS FOR EQUIVALENCE CLASS "
                        + equivalenceClass.toString());
                logger.logImportant("Number of hands played: " + ROLLOUTS_PER_EQUIV_CLASS);
                logger.logImportant("Number players: " + numberOfPlayers);
                logger.logImportant("Percentage of wins is " + percentageWin);
            }
        }
    }
}
