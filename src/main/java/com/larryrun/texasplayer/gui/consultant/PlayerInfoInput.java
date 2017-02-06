package com.larryrun.texasplayer.gui.consultant;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class PlayerInfoInput {
    private boolean isSelf;
    private boolean disabled;
    private boolean dealer;
    private boolean onTurn;

    private GridPane grid;
    private Label onTurnLabel;
    private CheckBox enableCheckBox;
    private RadioButton dealerRadioButton;
    private TextField playerNameField;
    private TextField[] cardFields;
    private Button cardSaveBtn;

    private PlayerInfoInput() {}

    public static PlayerInfoInput createPlayerMoveInputIn(GridPane containerGrid, int rowIdx, String playerLabel, Consumer<PlayerInfoInput> dealerSelected, boolean isSelf) {
        PlayerInfoInput moveInput = new PlayerInfoInput();
        moveInput.isSelf = isSelf;
        moveInput.grid = containerGrid;

        int columnIdx = 0;
        moveInput.onTurnLabel = new Label(" ");
        moveInput.grid.add(moveInput.onTurnLabel, columnIdx++, rowIdx);

        moveInput.enableCheckBox = new CheckBox();
        if(isSelf) {
            moveInput.enableCheckBox.setDisable(true);
            moveInput.enableCheckBox.setSelected(true);
        }
        moveInput.enableCheckBox.setOnAction(e -> moveInput.setDisabled(!moveInput.enableCheckBox.isSelected()));

        moveInput.grid.add(moveInput.enableCheckBox, columnIdx++, rowIdx);
        moveInput.grid.add(new Label(playerLabel + ": "), columnIdx++, rowIdx);

        moveInput.dealerRadioButton = new RadioButton();
        moveInput.dealerRadioButton.setOnAction(event -> dealerSelected.accept(moveInput));
        moveInput.grid.add(moveInput.dealerRadioButton, columnIdx++, rowIdx);

        moveInput.playerNameField = new TextField();
        moveInput.playerNameField.setPromptText("Player name");
        moveInput.grid.add(moveInput.playerNameField, columnIdx++, rowIdx);

        moveInput.cardFields = new TextField[]{new TextField(), new TextField()};
        moveInput.cardFields[0].setPromptText("Card1");
        moveInput.cardFields[1].setPromptText("Card2");
        moveInput.grid.add(moveInput.cardFields[0], columnIdx++, rowIdx);
        moveInput.grid.add(moveInput.cardFields[1], columnIdx++, rowIdx);

        moveInput.cardSaveBtn = new Button("Save");
        moveInput.grid.add(moveInput.cardSaveBtn, columnIdx++, rowIdx);

        moveInput.setDisabled(false);
        return moveInput;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;

        enableCheckBox.setSelected(!disabled);
        dealerRadioButton.setDisable(disabled);
        playerNameField.setDisable(disabled);
        cardFields[0].setDisable(disabled);
        cardFields[1].setDisable(disabled);
        cardSaveBtn.setDisable(disabled);
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDealer(boolean dealer) {
        this.dealer = dealer;

        dealerRadioButton.setSelected(dealer);
    }

    public boolean isDealer() {
        return dealer;
    }

    public void setOnTurn(boolean onTurn) {
        this.onTurn = onTurn;
        String text = onTurn? ">": " ";
        onTurnLabel.setText(text);
    }

}
