package com.larryrun.texasplayer.model;

import com.larryrun.texasplayer.controller.GameHandController;
import com.larryrun.texasplayer.controller.HandStrengthEvaluator;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;

public class GameRound {
    private GameHand gameHand;
    private GameProperties gameProperties;
    private HandStrengthEvaluator handStrengthEvaluator;

    private int toPlay;
    private int turn;
    private int numberOfPlayersAtBeginningOfRound;

    public GameRound(GameHand gameHand, GameProperties gameProperties, HandStrengthEvaluator handStrengthEvaluator) {
        this.gameHand = gameHand;
        this.gameProperties = gameProperties;
        this.handStrengthEvaluator = handStrengthEvaluator;

        gameHand.nextRound();
        toPlay = gameHand.getPlayersCount();
        if (gameHand.getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            takeBlinds(gameHand);
            toPlay--; // Big blinds don't have to call on himself if no raise :)
        }

        turn = 1;
        numberOfPlayersAtBeginningOfRound = gameHand.getPlayersCount();
    }

    public boolean playSinglePlayerMove() {
        if(toPlay > 0) {
            Player player = gameHand.getNextPlayer();
            BettingDecision bettingDecision = player.decide(gameHand);

            // We can't raise at second turn
            if (turn > numberOfPlayersAtBeginningOfRound && bettingDecision.equals(BettingDecision.RAISE)) {
                bettingDecision = BettingDecision.CALL;
            }

            // After a raise, every active players after the raiser must play
            if (bettingDecision.equals(BettingDecision.RAISE)) {
                toPlay = gameHand.getPlayersCount() - 1;
            }

            applyDecision(gameHand, player, bettingDecision);

            turn++;
            toPlay--;
        }

        // Check if we have a winner
        if (gameHand.getPlayersCount() == 1) {
            Player winner = gameHand.getCurrentPlayer();
            winner.addMoney(gameHand.getTotalBets());
            return true;
        }
        return false;
    }


}
