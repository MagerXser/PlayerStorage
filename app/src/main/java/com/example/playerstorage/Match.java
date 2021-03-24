package com.example.playerstorage;

public class Match {

    public Match(int matchNumber, int team, int otherTeam, int teamScore, int otherTeamScore)
    {
        MatchNumber = matchNumber;
        Team = team;
        OtherTeam = otherTeam;
        TeamScore = teamScore;
        OtherTeamScore = otherTeamScore;
    }

    public Integer MatchNumber;
    public Integer Team;
    public Integer OtherTeam;
    public Integer TeamScore;
    public Integer OtherTeamScore;
}
