package com.example.playerstorage;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerHelper {

    public static ArrayList<Player> getPlayers(Cursor playersTable) {
        ArrayList<Player> players = new ArrayList<>();
        while (playersTable.moveToNext()) {
            String name = playersTable.getString(1);
            String gender = playersTable.getString(2);
            String elo = playersTable.getString(3);
            String team = playersTable.getString(4);
            String hand = playersTable.getString (6);

            players.add(new Player(name, gender, elo, team, hand));
        }

        return players;
    }

    public static ArrayList<Player> getMales(ArrayList<Player> players) {
        ArrayList<Player> males = new ArrayList<>();

        for (Player player : players) {
            if (player.Gender.toLowerCase().equals("male")) {
                males.add(player);
            }
        }

        return males;
    }

    public static ArrayList<Player> getFemales(ArrayList<Player> players) {
        ArrayList<Player> males = new ArrayList<>();

        for (Player player : players) {
            if (player.Gender.toLowerCase().equals("female")) {
                males.add(player);
            }
        }

        return males;
    }

    public static Player getPlayer(ArrayList<Player> players, String name)
    {
        for (Player player : players)
        {
            if (player.Name.equals(name))
            {
                return player;
            }
        }

        return null;
    }

    public static ArrayList<String> getNames(ArrayList<Player> players) {
        ArrayList<String> names = new ArrayList<>();
        for (Player player : players)
        {
            names.add(player.Name);
        }

        return names;
    }

    public static ArrayList<Player> getPlayersInTeam(ArrayList<Player> players, int team){
        ArrayList<Player> teamMembers = new ArrayList<>();

        for (Player player : players) {
            if (player.Team == team) {
                teamMembers.add(player);
            }
        }

        return teamMembers;
    }

    public static ArrayList<Player> generateTeams(ArrayList<Player> players) {
        Collections.shuffle(players);

        int teamNumber = 0;
        int teamSize = 2;

        for (int i = 0; i < players.size(); i ++)
        {
            Player player = players.get(i);
            player.Team = teamNumber;
            if ((i + 1) % teamSize == 0) {
                teamNumber++;
            }
        }

        return players;
    }

    public static HashMap<Integer, ArrayList<Player>> getTeams(ArrayList<Player> players) {
        HashMap<Integer, ArrayList<Player>> teams = new HashMap<>();

        for (Player player : players) {
            ArrayList<Player> teamPlayers = teams.get(player.Team);

            if (teamPlayers == null) {
                teamPlayers = new ArrayList<>();
            }

            teamPlayers.add(player);
            teams.put(player.Team, teamPlayers);
        }

        return teams;
    }

    public static ArrayList<String> getTeamIds(HashMap<Integer, ArrayList<Player>> teams) {
        ArrayList<String> teamIds = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Player>> mapEntry : teams.entrySet())
        {
            int teamId = mapEntry.getKey();
            teamIds.add(String.valueOf(teamId));
        }

        return teamIds;
    }

    public static ArrayList<String> getExtendedTeamInformation(HashMap<Integer, ArrayList<Player>> teams){
        ArrayList<String> extendedTeamInformation = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Player>> mapEntry : teams.entrySet())
        {
            ArrayList<Player> players = mapEntry.getValue();
            ArrayList<String> playerNames = PlayerHelper.getNames(players);

            String extendedSingleTeamInformation = "Team " + mapEntry.getKey() + " => \n" + getCombinedPlayers(playerNames);

            extendedTeamInformation.add(extendedSingleTeamInformation);
        }

        return extendedTeamInformation;
    }

    public static String getExtendedSingleTeamInformation(HashMap<Integer, ArrayList<Player>> teams, int teamId) {
        for (Map.Entry<Integer, ArrayList<Player>> mapEntry : teams.entrySet())
        {
            if (mapEntry.getKey() != teamId) {
                continue;
            }

            ArrayList<Player> players = mapEntry.getValue();
            ArrayList<String> playerNames = PlayerHelper.getNames(players);

            return getCombinedPlayers(playerNames);
        }

        return "";
    }

    public static String getCombinedPlayers(ArrayList<String> playerNames) {
        StringBuilder combinedPlayers = new StringBuilder();
        for (String playerName : playerNames)
        {
            combinedPlayers.append(playerName).append("\n");
        }

        return String.format("%s", combinedPlayers.toString());
    }
}
