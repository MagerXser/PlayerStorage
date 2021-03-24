package com.example.playerstorage;

import android.os.Bundle;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlayerMatchActivity extends AppCompatActivity {

    private TextView Team1, Team2, Team1Players, Team2Players;
    private EditText Team1Score, Team2Score;
    private Button enterScore;

    private DatabaseHelper databaseHelper;
    private int matchNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view layout to activity_playermatch
        setContentView(R.layout.activity_playermatch);

        // Initialise and check if matchNumber is passed
        if (Initialisation()) return;

        // Set all information on page
        if (!SetListViewInformation()) return;

        // Set onClickListeners
        SetClickListeners();
    }

    private boolean Initialisation() {
        Team1 = findViewById(R.id.Team0);
        Team2 = findViewById(R.id.Team1);
        Team1Players = findViewById(R.id.Team1Players);
        Team2Players = findViewById(R.id.Team2Players);
        Team1Score = findViewById(R.id.Team1Score);
        Team2Score = findViewById(R.id.Team2Score);
        enterScore = findViewById(R.id.enterScore);

        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) return true;

        matchNumber = extras.getInt("matchNumber");
        return false;
    }

    private boolean SetListViewInformation() {
        ArrayList<Player> players = databaseHelper.getPlayers();
        ArrayList<Match> matches = databaseHelper.getMatches();
        Match match = MatchHelper.getMatchByMatchNumber(matches, matchNumber);

        if (match == null)
        {
            Toast.makeText(getApplicationContext(), "This match is invalid", Toast.LENGTH_SHORT).show();
            this.finish();
            return false;
        }

        int firstTeam = match.Team;
        int secondTeam = match.OtherTeam;

        ArrayList<Player> firstTeamPlayers = PlayerHelper.getPlayersInTeam(players, firstTeam);
        ArrayList<String> firstTeamPlayerNames = PlayerHelper.getNames(firstTeamPlayers);

        ArrayList<Player> secondTeamPlayers = PlayerHelper.getPlayersInTeam(players, secondTeam);
        ArrayList<String> secondTeamPlayerNames = PlayerHelper.getNames(secondTeamPlayers);

        Team1.setText("Team " + match.Team);
        Team1Players.setText(PlayerHelper.getCombinedPlayers(firstTeamPlayerNames));

        Team2.setText("Team " + match.OtherTeam);
        Team2Players.setText(PlayerHelper.getCombinedPlayers(secondTeamPlayerNames));

        return true;
    }

    private void SetClickListeners() {
        enterScore.setOnClickListener(v -> {
            if (Team1Score.length() == 0 || Team2Score.length() == 0) {

                Toast.makeText(getApplicationContext(), "Error => Both fields need to be entered" , Toast.LENGTH_SHORT).show();
                return;
            }

            String teamScore = Team1Score.getText().toString();
            String otherTeamScore = Team2Score.getText().toString();

            ArrayList<Match> matches = databaseHelper.getMatches();

            Match match = MatchHelper.getMatchByMatchNumber(matches, matchNumber);

            match.TeamScore = Integer.parseInt(teamScore);
            match.OtherTeamScore = Integer.parseInt(otherTeamScore);

            databaseHelper.updateMatch(match);

            String messageToShow;
            if (ScoreHelper.CheckIfThereIsAWinner(match.TeamScore, match.OtherTeamScore))
            {
                int winner = ScoreHelper.GetWinner(match);
                messageToShow = "The Winner is Team " + winner + "!!!";
                SetNewElo(match, winner);
            }
            else{
                messageToShow = "Team " + match.Team + ": " + match.TeamScore + "\n" + "Team " + match.OtherTeam + ": " + match.OtherTeamScore;
            }
            Toast.makeText(getApplicationContext(), messageToShow, Toast.LENGTH_SHORT).show();
        });
    }

    private void SetSinglesNewElo(Match match, int winner){
        ArrayList<Player> players = databaseHelper.getPlayers();

        Player firstPlayer = PlayerHelper.getPlayersInTeam(players, match.Team).get(0);
        Player secondPlayer = PlayerHelper.getPlayersInTeam(players, match.OtherTeam).get(0);

        // Elo Calculation
        Pair<Integer, Integer> elos = ScoreHelper.CalculateElo(firstPlayer.Elo, secondPlayer.Elo, winner == match.Team);
        int firstPlayerNewElo = elos.first;
        int secondPlayerNewElo = elos.second;

        System.out.println(firstPlayer.Name + "'s elo went from " + firstPlayer.Elo + " to " + firstPlayerNewElo);
        System.out.println(secondPlayer.Name + "'s elo went from " + secondPlayer.Elo + " to " + secondPlayerNewElo);

        firstPlayer.Elo = firstPlayerNewElo;
        databaseHelper.updatePlayer(firstPlayer);

        secondPlayer.Elo = secondPlayerNewElo;
        databaseHelper.updatePlayer(secondPlayer);
    }

    private void SetNewElo(Match match, int winner){
        ArrayList<Player> players = databaseHelper.getPlayers();

        // Team 1
        ArrayList<Player> firstTeamPlayers = PlayerHelper.getPlayersInTeam(players, match.Team);
        Player firstTeamFirstPlayer = firstTeamPlayers.get(0);
        Player firstTeamSecondPlayer = firstTeamPlayers.get(1);

        int firstTeamOldElo = (firstTeamFirstPlayer.Elo + firstTeamSecondPlayer.Elo) / 2;

        // Team 2
        ArrayList<Player> secondTeamPlayers = PlayerHelper.getPlayersInTeam(players, match.OtherTeam);
        Player secondTeamFirstPlayer = secondTeamPlayers.get(0);
        Player secondTeamSecondPlayer = secondTeamPlayers.get(1);

        int secondTeamOldElo = (secondTeamFirstPlayer.Elo + secondTeamSecondPlayer.Elo) / 2;

        // Elo Calculation
        Pair<Integer, Integer> elos = ScoreHelper.CalculateElo(firstTeamOldElo, secondTeamOldElo, winner == match.Team);
        int firstTeamNewElo = elos.first;
        int secondTeamNewElo = elos.second;

        // Use New Team Elo to set player's elo
        Pair<Integer, Integer> firstTeamElos = ScoreHelper.GetNewElo(firstTeamOldElo, firstTeamNewElo, firstTeamFirstPlayer.Elo, firstTeamSecondPlayer.Elo);
        int firstTeamFirstPlayerElo = firstTeamElos.first;
        int firstTeamSecondPlayerElo = firstTeamElos.second;

        System.out.println("Team 1 " + firstTeamFirstPlayer.Name + "'s elo went from " + firstTeamFirstPlayer.Elo + " to " + firstTeamFirstPlayerElo);
        System.out.println("Team 1 " + firstTeamSecondPlayer.Name + "'s elo went from " + firstTeamSecondPlayer.Elo + " to " + firstTeamSecondPlayerElo);

        firstTeamFirstPlayer.Elo = firstTeamFirstPlayerElo;
        databaseHelper.updatePlayer(firstTeamFirstPlayer);

        firstTeamSecondPlayer.Elo = firstTeamSecondPlayerElo;
        databaseHelper.updatePlayer(firstTeamSecondPlayer);

        Pair<Integer, Integer> secondTeamElos = ScoreHelper.GetNewElo(secondTeamOldElo, secondTeamNewElo, secondTeamFirstPlayer.Elo, secondTeamSecondPlayer.Elo);

        int secondTeamFirstPlayerElo = secondTeamElos.first;
        int secondTeamSecondPlayerElo = secondTeamElos.second;

        System.out.println("Team 2 " + secondTeamFirstPlayer.Name + "'s elo went from " + secondTeamFirstPlayer.Elo + " to " + secondTeamFirstPlayerElo);
        System.out.println("Team 2 " + secondTeamSecondPlayer.Name + "'s elo went from " + secondTeamSecondPlayer.Elo + " to " + secondTeamSecondPlayerElo);

        secondTeamFirstPlayer.Elo = secondTeamFirstPlayerElo;
        databaseHelper.updatePlayer(secondTeamFirstPlayer);

        secondTeamSecondPlayer.Elo = secondTeamSecondPlayerElo;
        databaseHelper.updatePlayer(secondTeamSecondPlayer);
    }
}
