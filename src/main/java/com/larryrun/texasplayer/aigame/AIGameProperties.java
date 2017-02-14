package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIAgressive;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIConservative;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.event.PlayerBalanceChanged;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;

import javax.inject.Inject;

public class AIGameProperties extends GameProperties {

    @Inject
    public AIGameProperties(final PlayerControllerHuman playerControllerHuman,
                            final PlayerControllerAI playerControllerAI,
                            final GameEventDispatcher gameEventDispatcher) {
        super(0, 10000, 20, 10, gameEventDispatcher);
        Player player1 = new Player(1, getInitialMoney(), playerControllerAI, gameEventDispatcher);
        addPlayer(player1);
        Player player2 = new Player(2, getInitialMoney(), playerControllerAI, gameEventDispatcher);
        addPlayer(player2);
        Player human = new Player(3, getInitialMoney(), playerControllerHuman, gameEventDispatcher);
//        Player human = new Player(3, getInitialMoney(), playerControllerAI, gameEventDispatcher);
        addPlayer(human);
    }

}
