package main.java.biz.tis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import main.java.biz.tis.model.Player;

public class HighScoreResults {

    private static final String STOP_WORD = "osu!";

    public static void main(String[] args) {
        System.out.println("Please enter valid duels! : ");
        Scanner input = new Scanner(System.in);
        String line = input.nextLine();

        Map<String, List<Player>> scoreMap = new HashMap<>();

        while (!line.equalsIgnoreCase(STOP_WORD) && input.hasNextLine()) {

            String[] players = line.split("<->");
            String[] firstPlayer = players[0].split("\\s+");
            String[] secondPlayer = players[1].split("\\s+");

            String nameOfTheFirstPlayer = firstPlayer[1];
            String nameOfTheSecondPlayer = secondPlayer[0];
            Integer scoreOfTheFirstPlayer = Integer.valueOf(firstPlayer[0]);
            Integer scoreOfTheSecondPlayer = Integer.valueOf(secondPlayer[1]);

            if (!scoreMap.containsKey(nameOfTheFirstPlayer)) {
                scoreMap.put(nameOfTheFirstPlayer, new ArrayList<>());
            }
            scoreMap.get(nameOfTheFirstPlayer)
                .add(new Player(nameOfTheSecondPlayer,
                    scoreOfTheFirstPlayer - scoreOfTheSecondPlayer));

            if (!scoreMap.containsKey(nameOfTheSecondPlayer)) {
                scoreMap.put(nameOfTheSecondPlayer, new ArrayList<>());
            }
            scoreMap.get(nameOfTheSecondPlayer)
                .add(new Player(nameOfTheFirstPlayer, scoreOfTheSecondPlayer - scoreOfTheFirstPlayer));

            line = input.nextLine();
        }

        List<Player> totalScores = new ArrayList<>();
        scoreMap.forEach((playerName, duels) -> {
            Integer totalScore = getTotalScore(duels);
            totalScores.add(new Player(playerName, totalScore));
        });

        totalScores.sort(Comparator.comparing(Player::getScore).reversed());
        totalScores.forEach(player -> {
            System.out.printf("%n%s - (%d)%n", player.getName(), player.getScore());

            scoreMap.get(player.getName())
                .forEach(opponent -> System.out.printf("*   %s <-> %d%n", opponent.getName(), opponent.getScore()));
        });

        input.close();

    }

    private static Integer getTotalScore(List<Player> players) {
        return players
            .stream()
            .map(Player::getScore)
            .reduce(0, Integer::sum);
    }
}
