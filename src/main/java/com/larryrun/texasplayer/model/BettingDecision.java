package com.larryrun.texasplayer.model;

public class BettingDecision {
    public final static BettingDecision FOLD = new BettingDecision("FOLD", -1);
    public final static BettingDecision CALL = new BettingDecision("CALL", -1);

    private String action;
    private int amount;
    private BettingDecision(String action, int amount) {
        this.action = action;
        this.amount = amount;
    }

    public static BettingDecision raise(int amount) {
        return new BettingDecision("RAISE", amount);
    }

    public static BettingDecision call(int amount) {
        return new BettingDecision("CALL", amount);
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

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return action + (isRaise()? " " + amount : "");
    }
}
