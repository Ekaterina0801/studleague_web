package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(scope = InfoTeamResults.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InfoTeamResults {
    private int number;

    private Team team;

    private List<Integer> answers = new ArrayList<>();

    private int totalScore = 0;

    private List<Integer> questionNumbers = new ArrayList<>();

    private int countQuestions;

    private Tournament tournament;

    private List<Player> players = new ArrayList<>();
}

