package com.larryrun.texasplayer.model;

import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.cards.Deck;
import com.larryrun.texasplayer.model.event.HoleCardsDealt;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.model.opponentmodeling.ContextAction;
import com.larryrun.texasplayer.model.opponentmodeling.ContextInformation;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class GameHand {
    private final Deque<Player> players;
    private final Deck deck;
    private final List<Card> sharedCards = new ArrayList<Card>();
    private final List<BettingRound> bettingRounds = new ArrayList<BettingRound>();
    private Boolean hasRemoved = true;
    private GameEventDispatcher gameEventDispatcher;

    public GameHand(List<Player> players, GameEventDispatcher gameEventDispatcher) {
        this.players = new LinkedList<>(players);
        this.gameEventDispatcher = gameEventDispatcher;

        deck = new Deck();
    }

    public void nextRound() {
        bettingRounds.add(new BettingRound());

        if (getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            dealHoleCards();
        } else if (getBettingRoundName().equals(BettingRoundName.POST_FLOP)) {
            dealFlopCards();
        } else {
            dealSharedCard();
        }
    }

    public Player getNextPlayer() {
        if (!hasRemoved) {
            Player player = players.removeFirst();
            players.addLast(player);
        }
        hasRemoved = false;
        return getCurrentPlayer();
    }

    public int getTotalBets() {
        int totalBets = 0;
        for (BettingRound bettingRound : bettingRounds) {
            totalBets += bettingRound.getTotalBets();
        }
        return totalBets;
    }

    public BettingRoundName getBettingRoundName() {
        return BettingRoundName.fromRoundNumber(bettingRounds.size());
    }

    public Player getCurrentPlayer() {
        return players.getFirst();
    }

    public List<Card> getSharedCards() {
        return sharedCards;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public BettingRound getCurrentBettingRound() {
        return bettingRounds.get(bettingRounds.size() - 1);
    }

    public List<BettingRound> getBettingRounds() {
        return bettingRounds;
    }

    public void removeCurrentPlayer() {
        players.removeFirst();
        hasRemoved = true;
    }

    protected void dealHoleCards() {
        for (Player player : players) {
            Card hole1 = deck.removeTopCard();
            Card hole2 = deck.removeTopCard();

            gameEventDispatcher.fireEvent(new HoleCardsDealt(player, hole1, hole2));

            player.setHoleCards(hole1, hole2);
        }
    }

    private void dealFlopCards() {
        sharedCards.add(deck.removeTopCard());
        sharedCards.add(deck.removeTopCard());
        sharedCards.add(deck.removeTopCard());
    }

    private void dealSharedCard() {
        sharedCards.add(deck.removeTopCard());
    }

    public Deque<Player> getPlayers() {
        return this.players;
    }

    public void applyDecision(Player player, BettingDecision bettingDecision, GameProperties gameProperties, double handStrength) {
        BettingRound currentBettingRound = getCurrentBettingRound();
        double potOdds = calculatePotOdds(player);
        ContextAction contextAction = new ContextAction(player, bettingDecision, getBettingRoundName(),
                currentBettingRound.getNumberOfRaises(),
                getPlayersCount(), potOdds);
        ContextInformation contextInformation = new ContextInformation(contextAction, handStrength);

        currentBettingRound.applyDecision(contextInformation, gameProperties);

        if (bettingDecision.equals(BettingDecision.FOLD)) {
            removeCurrentPlayer();
        }
    }

    public double calculatePotOdds(Player player) {
        BettingRound currentBettingRound = getCurrentBettingRound();
        int amountNeededToCall = currentBettingRound.getHighestBet() - currentBettingRound.getBetForPlayer(player);
        return (double) amountNeededToCall / (amountNeededToCall + getTotalBets());
    }

    protected Deck getDeck() {
        return deck;
    }
}
