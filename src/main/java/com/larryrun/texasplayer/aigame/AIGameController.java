package com.larryrun.texasplayer.aigame;

import com.google.inject.Inject;
import com.larryrun.texasplayer.controller.GameHandController;
import com.larryrun.texasplayer.model.Game;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;

public class AIGameController {

    private final Game game;
    private final GameHandController gameHandController;

    @Inject
    public AIGameController(final GameHandController gameHandController, final GameProperties gameProperties) {
        this.gameHandController = gameHandController;

        game = new Game(gameProperties.getPlayers());
    }

    public void play() {
        gameHandController.play(game);
        game.setNextDealer();
    }

}
