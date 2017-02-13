package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIAgressive;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIConservative;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;

import javax.inject.Inject;

public class AIGameProperties extends GameProperties {

    @Inject
    public AIGameProperties(final PlayerControllerHuman playerControllerHuman,
                            final PlayerControllerAI playerControllerAI,
                            final GameEventDispatcher gameEventDispatcher) {
        super(0, 10000, 20, 10);

        addPlayer(new Player(1, getInitialMoney(), playerControllerHuman, gameEventDispatcher));
        addPlayer(new Player(2, getInitialMoney(), playerControllerAI, gameEventDispatcher));
    }

}
