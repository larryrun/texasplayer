package com.larryrun.texasplayer.gui;

import javafx.scene.control.Alert;

import static javafx.scene.control.Alert.AlertType.ERROR;

public class GUIUtils {

    public static void showErrorAlert(String message) {
        Alert alert = new Alert(ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);

        alert.showAndWait();
    }

}
