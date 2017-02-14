package com.larryrun.texasplayer.aigame;

import com.google.inject.Inject;
import com.larryrun.texasplayer.model.Game;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;

public class AIGameController {

    private final Game game;
    private final AIGameGameHandController aiGameGameHandController;
    private final PlayerControllerHuman playerControllerHuman;

    @Inject
    public AIGameController(final AIGameGameHandController gameHandController,
                            final GameProperties gameProperties,
                            final PlayerControllerHuman playerControllerHuman) {
        this.aiGameGameHandController = gameHandController;
        this.playerControllerHuman = playerControllerHuman;

        game = new Game(gameProperties.getPlayers());
    }

    public void play() {
        aiGameGameHandController.play(game);
        game.setNextDealer();
    }

    public AIGameGameHandController getAIGameGameHandController() {
        return aiGameGameHandController;
    }

    public PlayerControllerHuman getPlayerControllerHuman() {
        return playerControllerHuman;
    }
}
