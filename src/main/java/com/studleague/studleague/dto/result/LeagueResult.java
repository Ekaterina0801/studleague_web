package com.studleague.studleague.dto.result;


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

    private HashMap<Integer, Double> normalizedResultsByTour = new HashMap<>();

    private Double totalScore;

}
