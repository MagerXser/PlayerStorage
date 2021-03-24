package com.example.playerstorage;

public class Player {

    public Player(String name, String gender, Integer elo, Integer team, String hand)
    {
        Name = name;
        Gender = gender;
        Elo = elo;
        Team = team;
        Hand = hand;
    }

    public Player(String name, String gender, String elo, String team, String hand)
    {
        Name = name;
        Gender = gender;
        Elo = Integer.parseInt(elo);
        Team = Integer.parseInt(team);
        Hand = hand;
    }

    public String Name;
    public String Gender;
    public Integer Elo;
    public Integer Team;
    public String Hand;
}
