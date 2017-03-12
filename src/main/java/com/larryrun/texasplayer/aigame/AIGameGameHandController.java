package com.larryrun.texasplayer.aigame;

import com.google.common.collect.*;
import com.larryrun.texasplayer.controller.GameEventDispatcher;
import com.larryrun.texasplayer.controller.HandPowerRanker;
import com.larryrun.texasplayer.controller.HandStrengthEvaluator;
import com.larryrun.texasplayer.controller.StatisticsController;
import com.larryrun.texasplayer.controller.opponentmodeling.OpponentModeler;
import com.larryrun.texasplayer.model.*;
import com.larryrun.texasplayer.model.cards.Card;
import com.larryrun.texasplayer.model.event.*;
import com.larryrun.texasplayer.model.gameproperties.GameProperties;
import com.larryrun.texasplayer.utils.Logger;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class AIGameGameHandController {
    protected final Logger logger;
    private final HandPowerRanker handPowerRanker;
    private final GameProperties gameProperties;
    private final StatisticsController statisticsController;
    private final HandStrengthEvaluator handStrengthEvaluator;
    private final OpponentModeler opponentModeler;
    private final GameEventDispatcher gameEventDispatcher;

    @Inject
    public AIGameGameHandController(final Logger logger,
                                    final HandPowerRanker handPowerRanker,
                                    final GameProperties gameProperties,
                                    final StatisticsController statisticsController,
                                    final HandStrengthEvaluator handStrengthEvaluator,
                                    final OpponentModeler opponentModeler,
                                    final GameEventDispatcher gameEventDispatcher) {
        this.logger = logger;
        this.handPowerRanker = handPowerRanker;
        this.gameProperties = gameProperties;
        this.statisticsController = statisticsController;
        this.handStrengthEvaluator = handStrengthEvaluator;
        this.opponentModeler = opponentModeler;
        this.gameEventDispatcher = gameEventDispatcher;
    }

    private GameHand currentGameHand;
    public void play(Game game) {
        currentGameHand = createGameHand(game);
        startNewRound();
    }

    private GameHand createGameHand(Game game) {
        GameHand gameHand = new GameHand(game.getPlayers(), gameProperties, gameEventDispatcher);


        List<Player> players = game.getPlayers();
        Player sb = game.getPlayers().get(0),
                bb = game.getPlayers().get(1),
                //dealer is the last player
                dealer = game.getPlayers().get(players.size() - 1);
        gameEventDispatcher.fireEvent(new GameHandStarted(dealer, sb, bb));

        game.addGameHand(gameHand);
        return gameHand;
    }

    private int toPlay;
    private int numberOfPlayersAtBeginningOfRound;

    private int turn;
    private void startNewRound() {
        currentGameHand.nextRound();
        toPlay = currentGameHand.getPlayersCount();
        turn = 1;
        numberOfPlayersAtBeginningOfRound = currentGameHand.getPlayersCount();

        if (currentGameHand.getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            takeBlinds(currentGameHand);
            toPlay--; // Big blinds don't have to call on himself if no raise :)
        }

        playAIMoveUntilHumanPlayerTurn();

    }

    private void playAIMoveUntilHumanPlayerTurn() {
        while (toPlay > 0) {
            Player player = currentGameHand.getNextPlayer();

            gameEventDispatcher.fireEvent(new PlayerOnTurn(player));
            if(player.isHumanPlayer()) {
                break;
            }

            BettingDecision bettingDecision = player.decide(currentGameHand);

            // We can't raise at second turn
            if (turn > numberOfPlayersAtBeginningOfRound && bettingDecision.isRaise()) {
                bettingDecision = BettingDecision.CALL;
            }

            // After a raise, every active players after the raiser must play
            if (bettingDecision.isRaise()) {
                toPlay = currentGameHand.getPlayersCount();
            }

            applyDecision(currentGameHand, player, bettingDecision);
            turn++;
            toPlay--;
        }

        if(toPlay == 0) {
            if (currentGameHand.getPlayersCount() == 1) {
                Player winner = currentGameHand.getCurrentPlayer();
                winner.addMoney(currentGameHand.getTotalBets());

                gameEventDispatcher.fireEvent(new HandCompleted(Lists.newArrayList(winner), Collections.emptyList()));
            }else if(currentGameHand.getBettingRoundName().equals(BettingRoundName.POST_RIVER)) {
                showDown(currentGameHand);
            }else {
                startNewRound();
            }
        }
    }

    public void playHumanMove() {
        Player humanPlayer = currentGameHand.getCurrentPlayer();
        PlayerControllerHuman playerControllerHuman = (PlayerControllerHuman) humanPlayer.getPlayerController();
        BettingDecision humanBettingDecision = playerControllerHuman.decide(humanPlayer, currentGameHand);

        // We can't raise at second turn
        if (turn > numberOfPlayersAtBeginningOfRound && humanBettingDecision.isRaise()) {
            humanBettingDecision = BettingDecision.CALL;
        }

        // After a raise, every active players after the raiser must play
        if (humanBettingDecision.isRaise()) {
            toPlay = currentGameHand.getPlayersCount();
        }

        applyDecision(currentGameHand, humanPlayer, humanBettingDecision);
        toPlay--;
        turn++;

        playAIMoveUntilHumanPlayerTurn();
    }

    private void takeBlinds(GameHand gameHand) {
        Player smallBlindPlayer = gameHand.getNextPlayer();
        Player bigBlindPlayer = gameHand.getNextPlayer();

        gameHand.getCurrentBettingRound().placeBet(smallBlindPlayer, gameProperties.getSmallBlind());
        gameHand.getCurrentBettingRound().placeBet(bigBlindPlayer, gameProperties.getBigBlind());
    }

    private void applyDecision(GameHand gameHand, Player player, BettingDecision bettingDecision) {
        double handStrength = handStrengthEvaluator.evaluate(player.getHoleCards(), gameHand.getSharedCards(), gameHand.getPlayersCount());
        gameHand.applyDecision(player, bettingDecision, gameProperties, handStrength);
    }

    private List<List<Player>> getPlayerHandPowerRankList(GameHand gameHand) {
        Iterable<Player> activePlayers = gameHand.getPlayers();
        List<Card> sharedCards = gameHand.getSharedCards();

        TreeMultimap<HandPower, Player> handPowerRankMultimap = TreeMultimap.create(Ordering.natural(), Ordering.arbitrary());
        for (Player player : activePlayers) {
            List<Card> mergeCards = new ArrayList<>(player.getHoleCards());
            mergeCards.addAll(sharedCards);
            HandPower handPower = handPowerRanker.rank(mergeCards);

            handPowerRankMultimap.put(handPower, player);
        }

        return handPowerRankMultimap.asMap().descendingMap().values()
                .stream()
                .map(Lists::newArrayList)
                .collect(Collectors.<List<Player>>toList());
    }

    private Map<Integer, Set<Player>> getPotOfPlayersMap(GameHand gameHand) {
        TreeMultimap<Integer, Player> playerByBetMap = TreeMultimap.create(Ordering.natural(), Ordering.arbitrary());
        for (Player player : gameHand.getAllPlayers()) {
            int playerBet = gameHand.getPlayerTotalBets(player);
            if(playerBet > 0) {
                playerByBetMap.put(playerBet, player);
            }
        }

        Map<Integer, Set<Player>> potOfPlayersMap = new HashMap<>();
        int previousBet = 0;
        int restPlayerCount = playerByBetMap.values().size();
        for (Integer bet : playerByBetMap.keySet()) {
            int pot = (bet - previousBet) * restPlayerCount;

            Set<Player> potPlayers = new HashSet<>();
            playerByBetMap.asMap().tailMap(bet, true).values().forEach(potPlayers::addAll);

            potOfPlayersMap.put(pot, potPlayers);

            previousBet = bet;
            restPlayerCount -= playerByBetMap.get(bet).size();
        }
        return potOfPlayersMap;
    }

    private void showDown(GameHand gameHand) {
        List<List<Player>> playerHandPowerRankList = getPlayerHandPowerRankList(gameHand);
        Map<Integer, Set<Player>> potOfPlayersMap = getPotOfPlayersMap(gameHand);

        int totalBets = gameHand.getTotalBets();
        for (List<Player> sameHandPowerPlayers : playerHandPowerRankList) {
            if(totalBets <= 0)
                break;

            Map<Player, Integer> playerGainMap = new HashMap<>();

            for (Map.Entry<Integer, Set<Player>> potOfPlayersEntry : potOfPlayersMap.entrySet()) {
                Integer pot = potOfPlayersEntry.getKey();
                Set<Player> sameBetPlayers = potOfPlayersEntry.getValue();

                for (Player player : sameHandPowerPlayers) {
                    if(sameBetPlayers.contains(player)) {
                        int gain = pot / sameHandPowerPlayers.size();
                        totalBets -= gain;

                        Integer gainSum = playerGainMap.get(player);
                        if(gainSum == null) {
                            playerGainMap.put(player, gain);
                        }else {
                            playerGainMap.put(player, gain + gainSum);
                        }
                    }
                }
            }
            playerGainMap.entrySet().forEach((e)-> e.getKey().addMoney(e.getValue()));
        }

        gameEventDispatcher.fireEvent(new HandCompleted(playerHandPowerRankList.get(0), gameHand.getPlayers()));

        // Opponent modeling
        opponentModeler.save(gameHand);
    }
}
