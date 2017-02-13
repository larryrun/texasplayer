package com.larryrun.texasplayer.model.gameproperties;

import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIIBluff;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIINormal;
import com.larryrun.texasplayer.model.Player;

import javax.inject.Inject;

public class PhaseIIGameProperties extends GameProperties {
    @Inject
    public PhaseIIGameProperties(final PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                                 final PlayerControllerPhaseIIBluff playerControllerPhaseIIBluff,
                                 final GameEventDispatcher gameEventDispatcher) {
        super(1000, 1000, 20, 10, gameEventDispatcher);

        addPlayer(new Player(1, getInitialMoney(), playerControllerPhaseIIBluff, gameEventDispatcher));
        addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIIBluff, gameEventDispatcher));
        addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseIINormal, gameEventDispatcher));
        addPlayer(new Player(4, getInitialMoney(), playerControllerPhaseIINormal, gameEventDispatcher));
    }
}
