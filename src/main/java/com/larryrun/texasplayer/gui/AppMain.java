package com.larryrun.texasplayer.gui;

import com.larryrun.texasplayer.gui.consultant.ConsultantApp;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppMain {
    private final static Logger LOG = LoggerFactory.getLogger(AppMain.class);

    public static void main(String[] args) {
        Application.launch(ConsultantApp.class);
    }
}
