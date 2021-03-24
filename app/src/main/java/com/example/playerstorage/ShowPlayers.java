package com.example.playerstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ShowPlayers extends AppCompatActivity {

    private ImageButton homeBtn, calendarBtn, accountBtn, matchesBtn, playersBtn;
    private ListView listView;
    private FirebaseAuth fAuth;
    private Button ViewAllButton;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view layout to activity_main
        setContentView(R.layout.activity_main);

        // Initialise
        Initialisation();

        // Set onClickListeners
        setOnClickListener();

    }

    public void Logout(View view){
        FirebaseAuth.getInstance().signOut(); // Logout the user
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();

    }

    private void setOnClickListener() {
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

        ViewAllButton.setOnClickListener(v -> {
            ArrayList<Player> players = databaseHelper.getPlayers();

            ShowList(PlayerHelper.getNames(players));
        });
    }


    private void Initialisation() {
        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        calendarBtn = (ImageButton) findViewById(R.id.calendarBtn);
        accountBtn = (ImageButton) findViewById(R.id.accountBtn);
        matchesBtn = findViewById(R.id.matchesBtn);
        fAuth = FirebaseAuth.getInstance();
        ViewAllButton = findViewById(R.id.btnView);
        playersBtn = findViewById(R.id.ViewTeams);

        databaseHelper = new DatabaseHelper(ShowPlayers.this);
    }

    private void ShowList(ArrayList<String> listToShow) {
        Intent intent = new Intent(ShowPlayers.this, ListActivity.class);
        intent.putStringArrayListExtra("listToShow", listToShow);
        startActivity(intent);
    }
}