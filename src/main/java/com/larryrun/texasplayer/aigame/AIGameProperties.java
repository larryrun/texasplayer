package com.larryrun.texasplayer.aigame;

import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIAgressive;
import com.larryrun.texasplayer.controller.phase3.PlayerControllerPhaseIIIConservative;
import com.larryrun.texasplayer.aigame.PlayerControllerSelf;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;

import javax.inject.Inject;

public class AIGameProperties extends GameProperties {

    @Inject
    public AIGameProperties(final PlayerControllerSelf playerControllerSelf,
                            final PlayerControllerPhaseIIIAgressive playerControllerPhaseIIIAgressive,
                            final PlayerControllerPhaseIIIConservative playerControllerPhaseIIIConservative) {
        super(0, 10000, 20, 10);

        addPlayer(new Player(1, getInitialMoney(), playerControllerSelf));
        addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIIIAgressive));
//        addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseIIIConservative));
    }

}