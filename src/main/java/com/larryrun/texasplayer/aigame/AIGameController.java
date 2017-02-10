package com.larryrun.texasplayer.aigame;

import com.google.inject.Inject;
import com.larryrun.texasplayer.controller.GameHandController;
import com.larryrun.texasplayer.model.Game;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;

public class AIGameController {

    private final Game game;
    private final GameProperties gameProperties;
    private final GameHandController gameHandController;

    @Inject
    public AIGameController(final GameHandController gameHandController, final GameProperties gameProperties) {
        this.gameHandController = gameHandController;
        this.gameProperties = gameProperties;

        game = new Game(gameProperties.getPlayers());
    }

    public void playNext() {
        gameHandController.play(game);
        game.setNextDealer();
    }

    public Game getGame() {
        return game;
    }

    public GameHandController getGameHandController() {
        return gameHandController;
    }
}
