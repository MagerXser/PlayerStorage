package com.example.playerstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;

public class PlayerViewer extends AppCompatActivity {

    private TextView nameView, genderView, playingHandView, eloView;
    private DatabaseHelper databaseHelper;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_viewer);

        nameView = findViewById(R.id.nameView);
        genderView = findViewById(R.id.genderView);
        playingHandView = findViewById(R.id.playingHandView);
        eloView = findViewById(R.id.eloView);

        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) return;

        name = extras.getString("playerName");

        SetListViewInformation();
    }

    private void SetListViewInformation() {

        ArrayList<Player> players = databaseHelper.getPlayers();
        Player player = PlayerHelper.getPlayer(players, name);

        nameView.setText("Name: " + player.Name);
        genderView.setText("Gender: " + player.Gender);
        eloView.setText("Elo: " + player.Elo.toString());
        playingHandView.setText("Playing Hand: " + player.Hand);
    }

}