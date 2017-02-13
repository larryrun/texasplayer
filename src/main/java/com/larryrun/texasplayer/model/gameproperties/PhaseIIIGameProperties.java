package com.larryrun.texasplayer.model.gameproperties;

import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIIBluff;
import com.larryrun.texasplayer.controller.phase2.PlayerControllerPhaseIINormal;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIAgressive;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIConservative;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.event.GameEvent;

import javax.inject.Inject;

public class PhaseIIIGameProperties extends GameProperties {
    @Inject
    public PhaseIIIGameProperties(final PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                                  final PlayerControllerPhaseIIBluff playerControllerPhaseIIBluff,
                                  final PlayerControllerPhaseIIIAgressive playerControllerPhaseIIIAgressive,
                                  final PlayerControllerPhaseIIIConservative playerControllerPhaseIIIConservative,
                                  final GameEventDispatcher gameEventDispatcher) {
        super(1000, 1000, 20, 10);

        addPlayer(new Player(1, getInitialMoney(), playerControllerPhaseIIBluff, gameEventDispatcher));
        addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIINormal, gameEventDispatcher));
        addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseIIIAgressive, gameEventDispatcher));
        addPlayer(new Player(4, getInitialMoney(), playerControllerPhaseIIIConservative, gameEventDispatcher));
    }
}
