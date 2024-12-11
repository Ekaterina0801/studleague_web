package com.studleague.studleague.dto;


import com.studleague.studleague.entities.Team;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LeagueResult {

    private Team team;

    private HashMap<Integer, Double> resultsByTour = new HashMap<>();

    private Double totalScore;

}
