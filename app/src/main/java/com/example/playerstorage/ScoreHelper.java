package com.example.playerstorage;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreHelper {

    public static int ConvertRatingToElo(String rating)
    {
        int elo = 0;

        switch (rating) {
            case "Beginner":
                elo = 1000;
                break;
            case "Intermediate":
                elo = 1300;
                break;
            case "Pro":
                elo = 1500;
                break;
        }

        return elo;
    }

    public static ArrayList<Player> GetRanking(ArrayList<Player> players)
    {
        // Sort elo by low to high
        Collections.sort(players, new EloComparator());

        // From high to low
        Collections.sort(players, Collections.reverseOrder());

        return players;
    }

    public static class EloComparator implements Comparator<Player>{
        public int compare(Player player, Player nextPlayer)
        {
            return player.Elo.compareTo(nextPlayer.Elo);
        }
    }

    public static boolean CheckIfThereIsAWinner(int teamScore, int otherTeamScore) {
        return teamScore > 29
                || teamScore > 20 && teamScore - otherTeamScore > 1
                || otherTeamScore > 29
                || otherTeamScore > 20 && otherTeamScore - teamScore > 1;
    }

    public static int GetWinner(Match match) {
        int winner;
        if (match.TeamScore > match.OtherTeamScore) {
            winner = match.Team;
        }
        else{
            winner = match.OtherTeam;
        }
        return winner;
    }

    // String whoIsTheWinner
    // if GetWinner(match) == match.Team => whoIsTheWinner = "First";
    // else whoIsTheWinner = "Second";

    // individual
    // float firstPlayerNewElo = CalculateElo(player1.Elo, player2.Elo, whoIsTheWinner, true);
    // float secondPlayerNewElo = CalculateElo(player1.Elo, player2.Elo, whoIsTheWinner, false);

    // team
    // team 1, team 2
    // firstTeamOldElo = (team1Player1Elo + team1Player2Elo) / 2, secondTeamOldElo = (team2Player1Elo + team2Player2Elo) / 2;

    // float firstTeamNewElo = CalculateElo(firstTeamOldElo, secondTeamOldElo, whoIsTheWinner, true);
    // float secondTeamNewElo = CalculateElo(firstTeamOldElo, secondTeamOldElo, whoIsTheWinner, false);

    // Team 1
    // float team1Player1Elo = GetPlayerNewElo(firstTeamOldElo, firstTeamNewElo, team1Player1Elo);
    // float team1Player2Elo = GetPlayerNewElo(firstTeamOldElo, firstTeamNewElo, team1Player2Elo);

    // Team 2
    // float team2Player1NewElo = GetPlayerNewElo(secondTeamOldElo, secondTeamNewElo, team2Player1Elo);
    // float team2Player2NewElo = GetPlayerNewElo(secondTeamOldElo, secondTeamNewElo, team2Player2Elo);


    // Function to calculate Elo rating
    // K is a constant.
    // d determines whether Player A wins
    // or Player B.
    public static Pair<Integer, Integer> CalculateElo(float eloForFirstPlayer, float eloForSecondPlayer, Boolean isFirstTeamWinner)
    {
        int K = 30;

        // To calculate the Winning
        // Probability of Player B
        float Pb = Probability(eloForFirstPlayer, eloForSecondPlayer);

        // To calculate the Winning
        // Probability of Player A
        float Pa = Probability(eloForSecondPlayer, eloForFirstPlayer);

        // Case -1 When Player A wins
        // Updating the Elo Ratings

        if (isFirstTeamWinner)
        {
            eloForFirstPlayer = eloForFirstPlayer + K * (1 - Pa);
            eloForSecondPlayer = eloForSecondPlayer + K * (0 - Pb);
        }
        else {
            eloForFirstPlayer = eloForFirstPlayer + K * (0 - Pa);
            eloForSecondPlayer = eloForSecondPlayer + K * (1 - Pb);
        }

        eloForFirstPlayer = (float) (Math.round(eloForFirstPlayer * 1000000.0) / 1000000.0);
        eloForSecondPlayer = (float) (Math.round(eloForSecondPlayer * 1000000.0) / 1000000.0);

        System.out.println("Updated Ratings Win:-" + "Ra = " + eloForFirstPlayer + " Rb = " + eloForSecondPlayer);

        return Pair.create((int) eloForFirstPlayer, (int) eloForSecondPlayer);
    }

    public static Pair<Integer, Integer> GetNewElo(int teamElo, int teamNewElo, int firstPlayerElo, int secondPlayerElo)
    {
        int eloDifference = teamNewElo - teamElo;

        int firstPlayerNewElo;
        int secondPlayerNewElo;

        firstPlayerNewElo = firstPlayerElo + eloDifference;
        secondPlayerNewElo = secondPlayerElo + eloDifference;

        return Pair.create((int) firstPlayerNewElo, (int) secondPlayerNewElo);
    }

    // Function to calculate the Probability
    private static float Probability(float rating1, float rating2)
    {
        return 1.0f * 1.0f / (1 + 1.0f *
                (float)(Math.pow(10, 1.0f *
                        (rating1 - rating2) / 400)));
    }
}
