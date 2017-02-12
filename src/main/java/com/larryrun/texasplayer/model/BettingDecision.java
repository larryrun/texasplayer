package com.larryrun.texasplayer.model;

public class BettingDecision {
    public final static BettingDecision CALL = new BettingDecision("CALL", -1);
    public final static BettingDecision FOLD = new BettingDecision("FOLD", -1);

    private String action;
    private int raiseAmount;
    private BettingDecision(String action, int amount) {
        this.action = action;
        this.raiseAmount = amount;
    }

    /**
     *
     * @param amount the amount of this raise, please notice that when this amount is < 0, this will be treated as a raise with minimum amount(e.g. one BB)
     * @return the RAISE BettingDecision
     */
    public static BettingDecision raise(int amount) {
        return new BettingDecision("RAISE", amount);
    }

    public boolean isCall() {
        return "CALL".equalsIgnoreCase(action);
    }

    public boolean isFold() {
        return "FOLD".equalsIgnoreCase(action);
    }

    public boolean isRaise() {
        return "RAISE".equalsIgnoreCase(action);
    }

    public int getRaiseAmount() {
        return raiseAmount;
    }

    @Override
    public String toString() {
        return action + (isRaise()? " " + raiseAmount: "");
    }
}
