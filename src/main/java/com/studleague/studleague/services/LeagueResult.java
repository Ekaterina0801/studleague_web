package com.studleague.studleague.services;


import com.studleague.studleague.entities.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeagueResult {

    private Team team;

    private HashMap<Integer, Double> resultsByTour = new HashMap<>();

    private Double totalScore;

}
