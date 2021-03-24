package com.example.playerstorage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> listToShow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view layout to activity_list
        setContentView(R.layout.activity_list);

        // Initialise and check if listToShow is passed
        if (Initialisation()) return;

        // Set listAdapter to show the list that is passed
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listToShow);
        listView.setAdapter(adapter);

        // Set onItemClickListener
        listView.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent (ListActivity.this, PlayerViewer.class);

            String name = listView.getItemAtPosition(position).toString();
            intent.putExtra("playerName", name);
            startActivity(intent);
        });
    }

    private boolean Initialisation() {
        listView = findViewById(R.id.listView);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return true;
        }

        listToShow = extras.getStringArrayList("listToShow");
        return false;
    }
}
