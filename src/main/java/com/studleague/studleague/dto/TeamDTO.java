package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDTO {
    private Long id;

    @JsonProperty("name")
    private String teamName;

    private String university;

    private LeagueDTO league;

    private Long idSite;

    @Builder.Default
    private List<PlayerDTO> players = new ArrayList<>();

    @Builder.Default
    private List<TournamentDTO> tournaments = new ArrayList<>();


}

