package com.larryrun.texasplayer.gui.aigame;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PlayerInfoPane extends HBox {
    private Label onTurnLabel, roleLabel, balanceLabel, actionLabel, frontMoneyLabel;

    public PlayerInfoPane(String name, GridPane outerContainer, int rowIdx) {
        onTurnLabel = new Label(" ");
        roleLabel = new Label();
        Label playerNameLabel = new Label(name);
        balanceLabel = new Label();
        actionLabel = new Label();
        frontMoneyLabel = new Label();

        outerContainer.add(onTurnLabel, 0, rowIdx);
        outerContainer.add(roleLabel, 1, rowIdx);
        outerContainer.add(playerNameLabel, 2, rowIdx);
        outerContainer.add(balanceLabel, 3, rowIdx);
        outerContainer.add(actionLabel, 4, rowIdx);
        outerContainer.add(frontMoneyLabel, 5, rowIdx);
    }

    public void fold() {
        actionLabel.setText("FOLD");
        frontMoneyLabel.setText("");
    }

    public void call(int amount) {
        actionLabel.setText("CALL");
        frontMoneyLabel.setText(String.format("%d", amount));
    }

    public void raise(int amount) {
        actionLabel.setText("RAISE");
        frontMoneyLabel.setText(String.format("%d", amount));
    }

    public void setRole(String role) {
        roleLabel.setText(role);
    }

    public void setBalance(int balance) {
        balanceLabel.setText(String.format("%d", balance));
    }

    public void setOnTurn(boolean onTurn) {
        onTurnLabel.setText(onTurn?">": " ");
    }

}
