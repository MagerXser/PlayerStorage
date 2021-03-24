package com.example.playerstorage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView listView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view layout to activity_list
        setContentView(R.layout.activity_list);

        // Initialise
        Initialisation();

        // Set all information on page
        SetListViewInformation();

        // Set onClickListeners
        setClickListeners(listView);
    }

    private void SetListViewInformation() {
        ArrayList<Player> players = databaseHelper.getPlayers();
        HashMap<Integer, ArrayList<Player>> teams = PlayerHelper.getTeams(players);

        ArrayList<String> extendedTeamInformation = PlayerHelper.getExtendedTeamInformation(teams);

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, extendedTeamInformation);
        listView.setAdapter(adapter);
    }

    private void Initialisation() {
        databaseHelper = new DatabaseHelper(TeamActivity.this);

        listView = findViewById(R.id.listView);
    }

    private void setClickListeners(ListView listView) {
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            int selectedTeam = Integer.parseInt(listView.getItemAtPosition(position).toString());

            ArrayList<Player> players = databaseHelper.getPlayers();
            HashMap<Integer, ArrayList<Player>> teams = PlayerHelper.getTeams(players);

            String text = PlayerHelper.getExtendedSingleTeamInformation(teams, selectedTeam);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return false;
        });

        listView.setOnItemClickListener((AdapterView<?> adapter1, View v, int position, long id) -> {
            Intent intent = new Intent(TeamActivity.this, PlayerMatchActivity.class);

            ArrayList<Match> matches = databaseHelper.getMatches();
            Match match = MatchHelper.getMatchByTeamId(matches, position);

            if (match == null) {
                Toast.makeText(getApplicationContext(), "This team does not have an opponent", Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra("matchNumber", match.MatchNumber);
            startActivity(intent);
        });
    }
}
