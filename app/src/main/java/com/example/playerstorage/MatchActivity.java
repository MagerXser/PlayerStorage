package com.example.playerstorage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MatchActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view layout to activity_list
        setContentView(R.layout.activity_list);

        //Initialise and check if
        DatabaseHelper databaseHelper = Initialisation();

        // Set all information on page
        SetListViewInformation(databaseHelper);

        //Set onClickListener
        onClickListener();
    }

    private void SetListViewInformation(DatabaseHelper databaseHelper) {
        ArrayList<Match> matches = databaseHelper.getMatches();
        ArrayList<String> extendedMatchInformation = MatchHelper.getExtendedMatchInformation(matches);

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, extendedMatchInformation);
        listView.setAdapter(adapter);
    }

    private void onClickListener() {
        listView.setOnItemClickListener((adapter1, v, position, id) -> {

            Intent intent = new Intent(MatchActivity.this, PlayerMatchActivity.class);

            intent.putExtra("matchNumber", position);
            startActivity(intent);
        });
    }

    private DatabaseHelper Initialisation() {
        DatabaseHelper databaseHelper = new DatabaseHelper(MatchActivity.this);

        listView = findViewById(R.id.listView);

        Log.d(MatchActivity.class.getName(), "populateListView: Displaying data in the ListView");
        return databaseHelper;
    }
}
