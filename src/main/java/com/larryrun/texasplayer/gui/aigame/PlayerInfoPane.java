package com.larryrun.texasplayer.gui.aigame;

import com.larryrun.texasplayer.model.BettingDecision;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class PlayerInfoPane extends HBox {
    private Label onTurnLabel, dealerLabel, blindLabel, roleLabel, balanceLabel, cardLabel, actionLabel, betMoneyLabel, winLabel;
    private int betMoney;

    public PlayerInfoPane(String name, GridPane outerContainer, int rowIdx) {
        onTurnLabel = new Label();
        dealerLabel = new Label(" ");
        blindLabel = new Label("  ");
        roleLabel = new Label();
        Label playerNameLabel = new Label(name);
        balanceLabel = new Label();
        cardLabel = new Label("       ");
        actionLabel = new Label("     ");
        betMoneyLabel = new Label();
        winLabel = new Label("   ");

        int columnIdx = 0;
        outerContainer.add(onTurnLabel, columnIdx++, rowIdx);
        outerContainer.add(dealerLabel, columnIdx++, rowIdx);
        outerContainer.add(blindLabel, columnIdx++, rowIdx);
        outerContainer.add(roleLabel, columnIdx++, rowIdx);
        outerContainer.add(playerNameLabel, columnIdx++, rowIdx);
        outerContainer.add(balanceLabel, columnIdx++, rowIdx);
        outerContainer.add(cardLabel, columnIdx++, rowIdx);
        outerContainer.add(actionLabel, columnIdx++, rowIdx);
        outerContainer.add(betMoneyLabel, columnIdx++, rowIdx);
        outerContainer.add(winLabel, columnIdx, rowIdx);
    }

    public void showBettingDecision(BettingDecision bettingDecision) {
        if(bettingDecision.isCall()) {
            actionLabel.setText("CALL");
        }else if(bettingDecision.isRaise()) {
            actionLabel.setText("RAISE");
        }else {
            actionLabel.setText("FOLD");
        }
    }

    public void setRole(String role) {
        roleLabel.setText(role);
    }

    public void setBalance(int balance) {
        balanceLabel.setText(String.format("%d", balance));
    }

    public void setOnTurn(boolean onTurn) {
        if(onTurn) {
            onTurnLabel.setText(">");
            actionLabel.setText("     ");
        }else {
            onTurnLabel.setText(" ");
        }
    }

    public void setHoleCardInfo(String cardInfo) {
        cardLabel.setText(cardInfo);
    }

    public void setDealer(boolean dealer) {
        if(dealer)
            dealerLabel.setText("D");
        else
            dealerLabel.setText(" ");

    }

    public void setBB(boolean bb) {
        if(bb)
            blindLabel.setText("BB");
        else
            blindLabel.setText("  ");
    }

    public void setSB(boolean sb) {
        if(sb) {
            blindLabel.setText("SB");
        } else {
            blindLabel.setText("  ");
        }
    }

    public void addBetMoney(int betMoney) {
        this.betMoney += betMoney;
        betMoneyLabel.setText(String.format("%d", this.betMoney));
    }

    public void setBetMoney(int betMoney) {
        this.betMoney = betMoney;
        betMoneyLabel.setText(String.format("%d", this.betMoney));
    }

    public void setWinner(boolean winner) {
        if(winner)
            winLabel.setText("WIN");
        else
            winLabel.setText("   ");
    }
}
