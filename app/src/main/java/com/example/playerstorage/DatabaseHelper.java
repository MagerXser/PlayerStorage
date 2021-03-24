package com.example.playerstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String PLAYERS_TABLE = "Name_Gender_Rating";
    public static final String Name = "Name";
    public static final String Gender = "Gender";
    public static final String Elo = "Elo"; // Rating
    public static final String Team = "Team"; // Team assignment
    public static final String PlayingHand = "Playing_Hand"; // What is their playing hand
//    public static final String Ranking = "Ranking";

    private static final String MATCHES_TABLE = "Matches";
    public static final String MatchNumber = "Match_Number"; // Match number
    public static final String OtherTeam = "Other_Team";
    public static final String TeamScore = "Team_Score";
    public static final String OtherTeamScore = "Other_Team_Score";

    private static final String EVENT_TABLE = "Events";
    public static final String Date = "Date";
    public static final String EventNumber = "Event_Number";
    public static final String Message = "Message";

    SQLiteDatabase db = this.getWritableDatabase();

    public DatabaseHelper(Context context){
        super(context, PLAYERS_TABLE, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + PLAYERS_TABLE + "(ID INTEGER PRIMARY KEY, " + Name + " TEXT, " + Gender + " TEXT, " + Elo + " INT, " + Team + " INT, " + MatchNumber + " INT, " + PlayingHand + " TEXT)";
        String matchesTable = "CREATE TABLE " + MATCHES_TABLE + "(ID INTEGER PRIMARY KEY, " + MatchNumber + " INT, " + Team + " INT, " + OtherTeam + " INT, " + TeamScore + " INT, " + OtherTeamScore + " INT)";
        String eventTable = "CREATE TABLE " + EVENT_TABLE + "(ID INTEGER PRIMARY KEY, " + Date + " TEXT, " + Message + " TEXT)";

        db.execSQL(createTable);
        db.execSQL(matchesTable);
        db.execSQL(eventTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLAYERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MATCHES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
        onCreate(db);
    }

    public boolean addPlayer(Player player) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Name, player.Name);
        contentValues.put(Gender, player.Gender);
        contentValues.put(Elo, player.Elo);
        contentValues.put(Team, player.Team);
        contentValues.put(PlayingHand, player.Hand);

        Log.d(DatabaseHelper.class.getName(), "addPlayer: Adding " + player.Name + " to " + PLAYERS_TABLE);

        long result = db.insert(PLAYERS_TABLE, null, contentValues);
        return result != -1;
    }

    public void updatePlayer(Player player) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Gender, player.Gender);
        contentValues.put(Elo, player.Elo);
        contentValues.put(Team, player.Team);

        db.update(PLAYERS_TABLE, contentValues, Name + " = ?", new String[]{player.Name});
    }

    public boolean deletePlayer(Player player)
    {
        return db.delete(PLAYERS_TABLE, Name + " =? ", new String[]{player.Name}) > 0;
    }

    public ArrayList<Player> getPlayers() {
        String query = "SELECT * FROM " + PLAYERS_TABLE;
        Cursor databaseResult = db.rawQuery(query, null);
        return PlayerHelper.getPlayers(databaseResult);
    }

    public boolean addMatch(Match match) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MatchNumber, match.MatchNumber);
        contentValues.put(Team, match.Team);
        contentValues.put(OtherTeam, match.OtherTeam);
        contentValues.put(TeamScore, match.TeamScore);
        contentValues.put(OtherTeamScore, match.OtherTeamScore);

        Log.d(DatabaseHelper.class.getName(), "addMatch: Adding " + match.MatchNumber + " to " + MATCHES_TABLE);

        long result = db.insert(MATCHES_TABLE, null, contentValues);
        return result != -1;
    }

    public void updateMatch(Match match) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Team, match.Team);
        contentValues.put(OtherTeam, match.OtherTeam);
        contentValues.put(TeamScore, match.TeamScore);
        contentValues.put(OtherTeamScore, match.OtherTeamScore);

        db.update(MATCHES_TABLE, contentValues, MatchNumber + " = ?", new String[]{match.MatchNumber.toString()});
    }

    public void clearMatchTable()
    {
        db.execSQL("DELETE FROM " + MATCHES_TABLE);
    }

    public boolean addEvent(Event event) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Date, event.Date);
        contentValues.put(Message, event.Message);

        Log.d(DatabaseHelper.class.getName(), "addEvent: Adding " + event.Date + " to " + EVENT_TABLE);

        long result = db.insert(EVENT_TABLE, null, contentValues);
        return result != -1;
    }

    public ArrayList<Match> getMatches(){
        String query = "SELECT * FROM " + MATCHES_TABLE;
        Cursor databaseResult = db.rawQuery(query, null);
        return MatchHelper.getMatches(databaseResult);
    }

    public void updateEvent(Event event)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Message, event.Message);

        db.update(EVENT_TABLE, contentValues, EventNumber + " = ?", new String[]{event.Date.toString()});
    }

    public boolean deleteEvent(Event event)
    {
        return db.delete(EVENT_TABLE, Date + " =? ", new String[]{event.Date}) > 0;
    }

    public ArrayList<Event> getEvents(){
        String query = "SELECT * FROM " + EVENT_TABLE;
        Cursor databaseResult = db.rawQuery(query, null);
        return EventHelper.getEvents(databaseResult);
    }
}
