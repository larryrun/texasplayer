package com.larryrun.texasplayer.model.gameproperties;

import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIIBluff;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIINormal;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIAgressive;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIConservative;
import com.larryrun.texasplayer.model.Player;

import javax.inject.Inject;

public class DemoGameProperties extends GameProperties {
    @Inject
    public DemoGameProperties(final PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                              final PlayerControllerPhaseIIBluff playerControllerPhaseIIBluff,
                              final PlayerControllerPhaseIIIAgressive playerControllerPhaseIIIAgressive,
                              final PlayerControllerPhaseIIIConservative playerControllerPhaseIIIConservative,
                              final GameEventDispatcher gameEventDispatcher) {
        super(15, 1000, 20, 10, gameEventDispatcher);

        addPlayer(new Player("player1", 1, getInitialMoney(), playerControllerPhaseIIBluff, gameEventDispatcher));
        addPlayer(new Player("player2", 2, getInitialMoney(), playerControllerPhaseIINormal, gameEventDispatcher));
        addPlayer(new Player("player3",3, getInitialMoney(), playerControllerPhaseIIIAgressive, gameEventDispatcher));
        addPlayer(new Player("player4", 4, getInitialMoney(), playerControllerPhaseIIIConservative, gameEventDispatcher));
    }
}
