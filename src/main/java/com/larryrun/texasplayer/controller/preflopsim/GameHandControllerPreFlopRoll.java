package com.larryrun.texasplayer.controller.preflopsim;

import com.google.inject.Inject;
import com.larryrun.texasplayer.controller.*;
import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.model.BettingRoundName;
import com.larryrun.texasplayer.model.Game;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.cards.EquivalenceClass;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.model.preflopsim.GameHandPreFlopRoll;
import com.larryrun.texasplayer.utils.Logger;

public class GameHandControllerPreFlopRoll extends GameHandController {

    @Inject
    public GameHandControllerPreFlopRoll(Logger logger,
                                         HandPowerRanker handPowerRanker,
                                         GameProperties gameProperties,
                                         StatisticsController statisticsController,
                                         HandStrengthEvaluator handStrengthEvaluator,
                                         OpponentModeler opponentModeler,
                                         GameEventDispatcher gameEventDispatcher) {
        super(logger, handPowerRanker, gameProperties, statisticsController, handStrengthEvaluator, opponentModeler, gameEventDispatcher);
    }

    public void play(Game game, EquivalenceClass equivalenceClass) {
        logger.log("-----------------------------------------");
        logger.log("Game Hand #" + (game.gameHandsCount() + 1));
        logger.log("-----------------------------------------");
        logger.log("-----------------------------------------");
        logger.log(equivalenceClass.toString());
        logger.log("-----------------------------------------");
        GameHand gameHand = createGameHand(game, equivalenceClass);

        Boolean haveWinner = false;
        while (!gameHand.getBettingRoundName().equals(
                BettingRoundName.POST_RIVER)
                && !haveWinner) {
            haveWinner = playRound(gameHand);
        }

        if (!haveWinner) {
            showDown(gameHand);
        }
    }

    private GameHand createGameHand(Game game, EquivalenceClass equivalenceClass) {
        GameHand gameHand = new GameHandPreFlopRoll(game.getPlayers(), gameProperties, equivalenceClass, gameEventDispatcher);
        game.addGameHand(gameHand);
        return gameHand;
    }

}
