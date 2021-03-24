package com.example.playerstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;

public class CalendarActivity extends AppCompatActivity {

    ImageButton homeBtn, calendarBtn, accountBtn, matchesBtn, playersBtn;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        homeBtn = findViewById(R.id.homeBtn);
        calendarBtn = findViewById(R.id.calendarBtn);
        accountBtn = findViewById(R.id.accountBtn);
        matchesBtn = findViewById(R.id.matchesBtn);
        playersBtn = findViewById(R.id.viewTeams);
        calendarView = findViewById(R.id.calendarView);

//        calendarView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                long test = calendarView.getDate();
//                Toast.makeText(getApplicationContext(), "Date is picked" , Toast.LENGTH_SHORT).show();
//            }
//        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String  curDay = String.valueOf(dayOfMonth);
                String  curYear = String.valueOf(year);
                String  curMonth = String.valueOf(month);

                String date = curYear+"/"+curMonth+"/"+curDay;

                Log.e("date", date);

                Intent intent = new Intent(CalendarActivity.this, EventActivity.class);
                intent.putExtra("Date", date);
                startActivity(intent);
            }
        });


        calendarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity( new Intent (getApplicationContext(), CalendarActivity.class));
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


}