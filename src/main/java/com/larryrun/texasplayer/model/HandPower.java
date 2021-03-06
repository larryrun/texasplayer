package com.larryrun.texasplayer.model;

import com.larryrun.texasplayer.model.cards.CardNumber;

import java.util.List;

public class HandPower implements Comparable<HandPower> {
    private final HandPowerType handPowerType;
    private final List<CardNumber> tieBreakingInformation;

    public HandPower(final HandPowerType handPowerType,
            final List<CardNumber> tieBreakingInformation) {
        this.handPowerType = handPowerType;
        this.tieBreakingInformation = tieBreakingInformation;
    }

    public int compareTo(HandPower other) {
        int typeDifference = handPowerType.getPower() - other.handPowerType.getPower();
        if (typeDifference == 0) {
            for (int i = 0; i < tieBreakingInformation.size(); i++) {
                int tieDifference = tieBreakingInformation.get(i).getPower()
                        - other.tieBreakingInformation.get(i).getPower();
                if (tieDifference != 0) {
                    return tieDifference;
                }
            }
            return 0;
        }

        return typeDifference;
    }

    @Override
    public String toString() {
        return handPowerType.toString() + " " + tieBreakingInformation.toString();
    }

    public HandPowerType getHandPowerType() {
        return handPowerType;
    }

    public List<CardNumber> getTieBreakingInformation() {
        return tieBreakingInformation;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        else if(obj instanceof HandPower) {
            return this.compareTo((HandPower)obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * handPowerType.hashCode() + 31 * tieBreakingInformation.hashCode();
    }
}
