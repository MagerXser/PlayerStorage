package com.example.playerstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class StorePlayerPage extends AppCompatActivity {

    private EditText NameText;
    private Spinner LevelSpinner, GenderSpinner, PlayingHandSpinner;
    private Button AddButton, ViewAllButton, ViewMaleButton, ViewFemaleButton;
    private ImageButton homeBtn, calendarBtn, accountBtn, matchesBtn, playersBtn;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view layout to activity_main
        setContentView(R.layout.activity_store_player_page);

        // Initialise all page elements
        Initialisation();

        // Set fixed gender drop down menu
        SetGenderDropDownMenu();

        // Set fixed level drop down menu
        SetLevelDropDownMenu();

        // Set Playing hand drop down menu
        SetPlayingHand();

        // Set onClickListeners
        SetClickListeners();
    }

    public void Logout (View view){
        FirebaseAuth.getInstance().signOut(); // Logout the user
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();

    }

    private void Initialisation() {
        NameText = findViewById(R.id.editname);
        LevelSpinner = findViewById(R.id.level);
        GenderSpinner = findViewById(R.id.GenderSpinner);
        PlayingHandSpinner = findViewById(R.id.playingHand);
        AddButton = findViewById(R.id.add);
        ViewAllButton = findViewById(R.id.btnView);
        ViewMaleButton = findViewById(R.id.viewMale);
        ViewFemaleButton = findViewById(R.id.viewFemale);
        homeBtn = findViewById(R.id.homeBtn);
        calendarBtn = findViewById(R.id.calendarBtn);
        accountBtn = findViewById(R.id.accountBtn);
        matchesBtn = findViewById(R.id.matchesBtn);
        playersBtn = findViewById(R.id.viewTeams);

        databaseHelper = new DatabaseHelper(StorePlayerPage.this);
    }

    private void SetGenderDropDownMenu() {
        //Drop Down Menu For Gender
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(StorePlayerPage.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Gender));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderSpinner.setAdapter(myAdapter);
    }

    private void SetLevelDropDownMenu() {
        //Drop Down Menu for rating
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(StorePlayerPage.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Level));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LevelSpinner.setAdapter(myAdapter);
    }

    private void SetPlayingHand() {
        //Drop Down Menu for playing hand
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(StorePlayerPage.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.PlayingHand));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PlayingHandSpinner.setAdapter(myAdapter);
    }

    private void SetClickListeners() {
        AddButton.setOnClickListener(v -> {
            if (NameText.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error => You need to write something" , Toast.LENGTH_SHORT).show();
                return;
            }

            String name = NameText.getText().toString();
            String gender = GenderSpinner.getSelectedItem().toString();
            String level = LevelSpinner.getSelectedItem().toString();
            String hand = PlayingHandSpinner.getSelectedItem().toString();

            Player player = new Player(name, gender, ScoreHelper.ConvertRatingToElo(level), -1, hand);

            if (databaseHelper.addPlayer(player)) {
                Toast.makeText(getApplicationContext(), "Success => Inserted", Toast.LENGTH_SHORT).show();
                NameText.setText("");
            } else {
                Toast.makeText(getApplicationContext(), "Success => Not inserted", Toast.LENGTH_SHORT).show();
            }
        });

        ViewMaleButton.setOnClickListener(v -> {
            ArrayList<Player> players = databaseHelper.getPlayers();
            ArrayList<Player> males = PlayerHelper.getMales(players);

            ShowList(PlayerHelper.getNames(males));
        });

        ViewFemaleButton.setOnClickListener(v -> {
            ArrayList<Player> players = databaseHelper.getPlayers();
            ArrayList<Player> females = PlayerHelper.getFemales(players);

            ShowList(PlayerHelper.getNames(females));
        });

        calendarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(StorePlayerPage.this, CalendarActivity.class);
                startActivity(intent);
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
                startActivity( new Intent (getApplicationContext(), ShowPlayers.class));
            }
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

    private void ShowList(ArrayList<String> listToShow) {
        Intent intent = new Intent(StorePlayerPage.this, ListActivity.class);
        intent.putStringArrayListExtra("listToShow", listToShow);
        startActivity(intent);
    }
}