package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityReference()
public class TeamMainInfoDTO {

    private Long id;

    @JsonProperty("name")
    private String teamName;

    private String university;

    private LeagueDTO league;

    private List<Long> tournamentIds;

    private Long idSite;
}
