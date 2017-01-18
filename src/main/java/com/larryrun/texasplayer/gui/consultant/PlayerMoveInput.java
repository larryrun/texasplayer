package com.larryrun.texasplayer.gui.consultant;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class PlayerMoveInput {
    private GridPane grid;
    private CheckBox enableCheckBox;
    private TextField playerNameField;
    private Button callBtn;
    private Button foldBtn;
    private Button raiseBtn;
    private TextField raiseField;

    private PlayerMoveInput() {}

    public static PlayerMoveInput createPlayerMoveInputIn(GridPane containerGrid, int rowIdx, String playerLabel) {
        PlayerMoveInput moveInput = new PlayerMoveInput();
        moveInput.grid = containerGrid;

        moveInput.enableCheckBox = new CheckBox();
        moveInput.grid.add(moveInput.enableCheckBox, 0, rowIdx);
        moveInput.grid.add(new Label(playerLabel + ": "), 1, rowIdx);

        moveInput.playerNameField = new TextField();
        moveInput.grid.add(moveInput.playerNameField, 2, rowIdx);

        moveInput.callBtn = new Button("Call");
        moveInput.grid.add(moveInput.callBtn, 3, rowIdx);

        moveInput.foldBtn = new Button("Fold");
        moveInput.grid.add(moveInput.foldBtn, 4, rowIdx);

        moveInput.raiseBtn = new Button("Raise");
        moveInput.grid.add(moveInput.raiseBtn, 5, rowIdx);

        moveInput.raiseField = new TextField();
        moveInput.grid.add(moveInput.raiseField, 6, rowIdx);

        return moveInput;
    }

}
