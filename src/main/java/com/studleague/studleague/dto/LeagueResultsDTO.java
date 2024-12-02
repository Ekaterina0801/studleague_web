package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.*;

import java.util.HashMap;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonIdentityReference()
public class LeagueResultsDTO {
    private Long teamId;
    private String teamName;
    private HashMap<Integer, Double> resultsByTour = new HashMap<>();
    private Double totalScore;
}
