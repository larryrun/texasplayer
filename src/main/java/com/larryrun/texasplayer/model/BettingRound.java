package com.larryrun.texasplayer.model;

import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.model.event.BetPlaced;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.model.opponentmodeling.ContextAction;
import com.larryrun.texasplayer.model.opponentmodeling.ContextInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BettingRound {
    private final Map<Player, Integer> playerBets = new HashMap<Player, Integer>();
    private final List<ContextInformation> contextInformations = new ArrayList<ContextInformation>();
    private int highestBet = 0;
    private GameEventDispatcher gameEventDispatcher;

    public BettingRound(GameEventDispatcher gameEventDispatcher) {
        this.gameEventDispatcher = gameEventDispatcher;
    }

    public void applyDecision(ContextInformation contextInformation, GameProperties gameProperties) {
        ContextAction contextAction = contextInformation.getContextAction();
        BettingDecision bettingDecision = contextAction.getBettingDecision();
        Player player = contextAction.getPlayer();

        if(bettingDecision.isRaise()) {
            placeBet(player, bettingDecision.getAmount());
        }else if(bettingDecision.isCall()) {
            placeBet(player, highestBet);
        }


        // Don't save context information for pre flop
        // Hand strength is always 0 b/c there's no shared cards
        if (!contextAction.getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            contextInformations.add(contextInformation);
        }
    }

    public void placeBet(Player player, int bet) {
        Integer playerBet = playerBets.get(player);

        int betAmount;
        if (playerBet == null) {
            betAmount = bet;
        } else {
            betAmount = bet - playerBet;
        }
        if(player.getMoney() < betAmount) {
            //when player does not have the required money, we will treat this as a ALL IN bet
            betAmount = player.getMoney();
        }
        player.removeMoney(betAmount);

        if (bet > highestBet) {
            highestBet = bet;
        } else if (bet < highestBet) {
            throw new IllegalArgumentException("You can't bet less than the higher bet");
        }

        playerBets.put(player, bet);
        gameEventDispatcher.fireEvent(new BetPlaced(player, betAmount));
    }

    public int getHighestBet() {
        return highestBet;
    }

    public List<ContextInformation> getContextInformations() {
        return contextInformations;
    }

    public int getBetForPlayer(Player player) {
        Integer bet = playerBets.get(player);
        if (bet == null) {
            return 0;
        }
        return bet;
    }

    public int getTotalBets() {
        int totalBets = 0;
        for (Integer bet : playerBets.values()) {
            totalBets += bet;
        }
        return totalBets;
    }

    public int getNumberOfRaises() {
        int numberOfRaises = 0;
        for (ContextInformation contextInformation : contextInformations) {
            if (contextInformation.getContextAction().getBettingDecision().isRaise()) {
                numberOfRaises++;
            }
        }
        return numberOfRaises;
    }

    public ContextAction getContextActionForPlayer(Player player) {
        for (int i = contextInformations.size(); i > 0; i--) {
            ContextInformation contextInformation = contextInformations.get(i - 1);
            ContextAction contextAction = contextInformation.getContextAction();

            if (contextAction.getPlayer().equals(player)) {
                return contextAction;
            }
        }

        return null;
    }
}
