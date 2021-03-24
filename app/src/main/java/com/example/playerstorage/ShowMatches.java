package com.example.playerstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowMatches extends AppCompatActivity {

    private ImageButton homeBtn, calendarBtn, accountBtn, matchesBtn, playersBtn;
    private Button RandomizeTeamButton, MatchesButton, ViewTeamButton;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_matches);

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        calendarBtn = (ImageButton) findViewById(R.id.calendarBtn);
        accountBtn = (ImageButton) findViewById(R.id.accountBtn);
        matchesBtn = findViewById(R.id.matchesBtn);
        playersBtn = findViewById(R.id.viewTeams);
        RandomizeTeamButton = findViewById(R.id.randomizeTeams);
        MatchesButton = findViewById(R.id.matches);
        ViewTeamButton = findViewById(R.id.showTeams);

        databaseHelper = new DatabaseHelper(ShowMatches.this);

        calendarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity( new Intent(getApplicationContext(), CalendarActivity.class));
            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity( new Intent (getApplicationContext(), Account.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity( new Intent (getApplicationContext(), StorePlayerPage.class));
            }
        });

        matchesBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity( new Intent (getApplicationContext(), ShowMatches.class));
            }
        });

        playersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), ShowPlayers.class));
            }
        });

        RandomizeTeamButton.setOnClickListener(v -> {
            ArrayList<Player> players = databaseHelper.getPlayers();
            ArrayList<Player> playersWithTeams = PlayerHelper.generateTeams(players);

            UpdatePlayersTable(playersWithTeams);

            // Get updated players table
            players = databaseHelper.getPlayers();
            HashMap<Integer, ArrayList<Player>> teams = PlayerHelper.getTeams(players);

            UpdateMatchesTable(teams);

            ViewTeamButton.performClick();
        });

        MatchesButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShowMatches.this, MatchActivity.class);
            startActivity(intent);
        });

        ViewTeamButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShowMatches.this, TeamActivity.class);
            startActivity(intent);
        });
    }

    private void UpdatePlayersTable(ArrayList<Player> players) {
        for (Player player : players)
        {
            databaseHelper.updatePlayer(player);
        }
    }

    private void UpdateMatchesTable(HashMap<Integer, ArrayList<Player>> teams) {
        // matchNumber = 0 => teamId = 0, 1; 2 <= teamsize
        // matchNumber = 1 => teamId = 2, 3; 4 <= teamsize
        // matchNumber = 2 => teamId = 4, 5; 6 <= teamsize
        // matchNumber = i => teamId = i*2, i*2+1; (i+1)*2 <= teamsize

        databaseHelper.clearMatchTable();
        for (int i = 0; (i+1)*2 <= teams.size() ; i++)
        {
            Match match = new Match(i, i*2, i*2 + 1, 0, 0);
            databaseHelper.addMatch(match);
        }
    }
}