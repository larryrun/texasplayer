package com.larryrun.texasplayer.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.larryrun.texasplayer.controller.PokerController;
import com.larryrun.texasplayer.dependencyinjection.GamePropertiesParameter;
import com.larryrun.texasplayer.dependencyinjection.LogLevel;
import com.larryrun.texasplayer.dependencyinjection.TexasModule;
import com.larryrun.texasplayer.gui.consultant.ConsultantApp;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppMain {
    private final static Logger LOG = LoggerFactory.getLogger(AppMain.class);

    public static void main(String[] args) {
        String gameP = "demo";
        if(args.length == 1){
            gameP = args[0];
        }
        Injector injector = Guice.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.fromString(gameP)));

        PokerController pokerController = injector.getInstance(PokerController.class);
        pokerController.play();

        Application.launch(ConsultantApp.class);
    }
}
