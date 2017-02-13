package com.larryrun.texasplayer.gui;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javafx.scene.control.Alert.AlertType.ERROR;

public class GUIUtils {
    private final static Logger LOG = LoggerFactory.getLogger(GUIUtils.class);

    public static void showErrorAlert(String message) {
        Alert alert = new Alert(ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);

        LOG.error(message);
        alert.showAndWait();
    }

}
