package com.larryrun.texasplayer.model;

import com.larryrun.texasplayer.controller.GameHandController;

public class GameRound {
    private GameHand gameHand;
    private GameHandController gameHandController;
    private int toPlay;
    private int turn;
    private int numberOfPlayersAtBeginningOfRound;


    public GameRound(GameHand gameHand, GameHandController gameHandController) {
        this.gameHand = gameHand;
        this.gameHandController = gameHandController;

        gameHand.nextRound();
        toPlay = gameHand.getPlayersCount();
        if (gameHand.getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            gameHandController.takeBlinds(gameHand);
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
            if (turn > numberOfPlayersAtBeginningOfRound
                    && bettingDecision.equals(BettingDecision.RAISE)) {
                bettingDecision = BettingDecision.CALL;
            }

            // After a raise, every active players after the raiser must play
            if (bettingDecision.equals(BettingDecision.RAISE)) {
                toPlay = gameHand.getPlayersCount() - 1;
            }

            gameHandController.applyDecision(gameHand, player, bettingDecision);
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
