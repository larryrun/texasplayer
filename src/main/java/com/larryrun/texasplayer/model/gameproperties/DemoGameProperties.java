package com.larryrun.texasplayer.model.gameproperties;

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
                              final PlayerControllerPhaseIIIConservative playerControllerPhaseIIIConservative) {
        super(15, 1000, 20, 10);

        addPlayer(new Player(1, getInitialMoney(), playerControllerPhaseIIBluff));
        addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIINormal));
        addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseIIIAgressive));
        addPlayer(new Player(4, getInitialMoney(), playerControllerPhaseIIIConservative));
    }
}
