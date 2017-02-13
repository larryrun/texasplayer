package com.larryrun.texasplayer.gui.aigame;

import com.larryrun.texasplayer.model.cards.Card;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PlayerInfoPane extends HBox {
    private Label onTurnLabel, roleLabel, balanceLabel, cardLabel, actionLabel, frontMoneyLabel;

    public PlayerInfoPane(String name, GridPane outerContainer, int rowIdx) {
        onTurnLabel = new Label(" ");
        roleLabel = new Label();
        Label playerNameLabel = new Label(name);
        balanceLabel = new Label();
        cardLabel = new Label("  ");
        actionLabel = new Label();
        frontMoneyLabel = new Label();

        int columnIdx = 0;
        outerContainer.add(onTurnLabel, columnIdx++, rowIdx);
        outerContainer.add(roleLabel, columnIdx++, rowIdx);
        outerContainer.add(playerNameLabel, columnIdx++, rowIdx);
        outerContainer.add(balanceLabel, columnIdx++, rowIdx);
        outerContainer.add(cardLabel, columnIdx++, rowIdx);
        outerContainer.add(actionLabel, columnIdx++, rowIdx);
        outerContainer.add(frontMoneyLabel, columnIdx, rowIdx);
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

    public void setHoleCardInfo(String cardInfo) {
        cardLabel.setText(cardInfo);
    }

}
