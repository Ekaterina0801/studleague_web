package com.studleague.studleague.dto;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InfoTeamResults {
    private int number;
    private Team team;
    private List<Integer> answers = new ArrayList<>();
    private int totalScore = 0;
    private List<Integer> questionNumbers = new ArrayList<>();
    private int countQuestions;
    private Tournament tournament;
    private List<Player> players = new ArrayList<>();
    public InfoTeamResults() {
    }

    public InfoTeamResults(int number, Team team, List<Integer> answers, int totalScore, List<Integer> questionNumbers,int countQuestions, Tournament tournament, List<Player> players) {
        this.number = number;
        this.team = team;
        this.answers = answers;
        this.totalScore = totalScore;
        this.questionNumbers = questionNumbers;
        this.countQuestions = countQuestions;
        this.tournament = tournament;
        this.players = players;
    }
}

