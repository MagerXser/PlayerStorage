package com.example.playerstorage;

import android.database.Cursor;

import java.util.ArrayList;

public class EventHelper {
    public static ArrayList<Event> getEvents (Cursor data)
    {
        ArrayList<Event> events = new ArrayList<>();
        while (data.moveToNext()) {
            String date = data.getString(1);
            String message = data.getString(2);

            events.add(new Event(date, message));
        }
        return events;
    }

    public static ArrayList<String> getDetailedEvents(ArrayList<Event> events){
        ArrayList<String> messages = new ArrayList<>();
        for (Event event: events)
        {
            String message = "Date: " + event.Date + " => \n" + event.Message;
            messages.add(message);
        }

        return messages;
    }

    public static ArrayList<Event> getEventsByDate(ArrayList<Event> events, String date)
    {
        ArrayList<Event> eventsOnThisDate = new ArrayList<>();

        for (Event event : events)
        {
            if (event.Date == date)
            {
                eventsOnThisDate.add(event);
            }
        }

        return eventsOnThisDate;
    }

    public static ArrayList<String> getMessagesByDate(ArrayList<Event> events, String date)
    {
        ArrayList<String> eventMessages = new ArrayList<>();

        for (Event event : events)
        {
            if (event.Date == date)
            {
                eventMessages.add(event.Message);
            }
        }

        return eventMessages;
    }
}
