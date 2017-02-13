package com.larryrun.texasplayer.model.preflopsim;

import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.model.GameHand;
import com.larryrun.texasplayer.model.Player;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.cards.Deck;
import com.larryrun.texasplayer.model.cards.EquivalenceClass;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;

import java.util.List;

public class GameHandPreFlopRoll extends GameHand {

    private final EquivalenceClass equivalenceClass;

    public GameHandPreFlopRoll(final List<Player> players,
                               final GameProperties gameProperties,
                               final EquivalenceClass equivalenceClass,
                               final GameEventDispatcher gameEventDispatcher) {
        super(players, gameProperties, gameEventDispatcher);
        this.equivalenceClass = equivalenceClass;
    }

    /**
     * Deals the hole cards. The prospective of the simulation is player0's one,
     * so players0's hole cards are the same of equivalence cards, while the
     * other players receive random cards form the top of the deck.
     */
    @Override
    protected void dealHoleCards() {
        Deck deck = getDeck();
        Player player1 = getPlayer1();
        getPlayers().remove(player1);

        List<Card> holeCards = this.equivalenceClass.equivalence2cards();
        Card hole1 = holeCards.get(0);
        Card hole2 = holeCards.get(1);
        player1.setHoleCards(hole1, hole2);
        deck.removeCard(hole1);
        deck.removeCard(hole2);

        // other players card are random
        for (Player player : this.getPlayers()) {
            hole1 = deck.removeTopCard();
            hole2 = deck.removeTopCard();
            player.setHoleCards(hole1, hole2);
        }

        getPlayers().add(player1);
    }

    private Player getPlayer1(){
        for (Player player : getPlayers()) {
            if (player.getNumber() == 1) {
                return player;
            }
        }
        throw new IllegalArgumentException("Must have a player #1 during rollout");
    }

}
