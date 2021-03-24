package com.example.playerstorage;

import android.database.Cursor;
import java.util.ArrayList;

public class MatchHelper {
    public static ArrayList<Match> getMatches (Cursor data) {
        ArrayList<Match> matches = new ArrayList<>();
        while (data.moveToNext()) {
            int matchNumber = data.getInt(1);
            int team = data.getInt(2);
            int otherTeam = data.getInt(3);
            int teamScore = data.getInt(4);
            int otherTeamScore = data.getInt(5);

            matches.add(new Match(matchNumber, team, otherTeam, teamScore, otherTeamScore));
        }
        return matches;
    }

    public static ArrayList<String> getMatchNumbers(ArrayList<Match> matches) {
        ArrayList<String> matchNumbers = new ArrayList<>();
        for (Match match : matches)
        {
            matchNumbers.add(match.MatchNumber.toString());
        }

        return matchNumbers;
    }

    public static ArrayList<String> getExtendedMatchInformation(ArrayList<Match> matches){
        ArrayList<String> extendedMatchInformation = new ArrayList<>();
        for (Match match : matches)
        {
            String extendedSingleMatchInformation = "Match " + match.MatchNumber + " => \n" + getCombinedTeams(match.Team.toString(), match.OtherTeam.toString());
            extendedMatchInformation.add(extendedSingleMatchInformation);
        }

        return extendedMatchInformation;
    }

    public static String getCombinedTeams(String team, String otherTeam)
    {
        return "Team " + team + "\n" + "Team " + otherTeam;
    }

    public static Match getMatchByTeamId(ArrayList<Match> matches, int teamId)
    {
        for (Match match : matches)
        {
            if (match.Team == teamId || match.OtherTeam == teamId)
            {
                return match;
            }
        }

        return null;
    }

    public static Match getMatchByMatchNumber(ArrayList<Match> matches, int matchNumber)
    {
        for (Match match : matches)
        {
            if (match.MatchNumber == matchNumber)
            {
                return match;
            }
        }

        return null;
    }
}
