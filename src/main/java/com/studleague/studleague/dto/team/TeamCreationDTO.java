package com.studleague.studleague.dto.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TeamCreationDTO {

    @JsonProperty("name")
    private String teamName;

    private String university;

    private Long leagueId;

    private List<Long> tournamentIds;

    private Long idSite;
}
