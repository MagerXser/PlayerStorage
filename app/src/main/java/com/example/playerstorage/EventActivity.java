package com.example.playerstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    private String date;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view layout to activity_list
        setContentView(R.layout.activity_event);

        //Initialise and check if
        DatabaseHelper databaseHelper = Initialisation();

        // Set all information on page
        SetListViewInformation(databaseHelper);

        listView = findViewById(R.id.listView);

        Bundle extras = getIntent().getExtras();
        if (extras == null) return;

        date = extras.getString("Date");
    }

    private void SetListViewInformation(DatabaseHelper databaseHelper) {
        ArrayList<Event> events = databaseHelper.getEvents();
        ArrayList<String> messages = EventHelper.getMessagesByDate(events, date);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        listView.setAdapter(adapter);
    }

    private DatabaseHelper Initialisation() {
        DatabaseHelper databaseHelper = new DatabaseHelper(EventActivity.this);

        listView = findViewById(R.id.listView);

        Log.d(EventActivity.class.getName(), "populateListView: Displaying data in the ListView");
        return databaseHelper;
    }
}