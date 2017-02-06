package com.larryrun.texasplayer.model.opponentmodeling;

public enum ContextPotOdds {
    LOW,
    HIGH;

    public static ContextPotOdds valueFor(double potOdds) {
        if (potOdds > 0.2) {
            return HIGH;
        } else {
            return LOW;
        }
    }
}
