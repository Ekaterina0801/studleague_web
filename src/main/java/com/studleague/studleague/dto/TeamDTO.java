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
    private long id;

    private String teamName;

    private String university;

    private long leagueId;

    private long idSite;

    @Builder.Default
    private List<Long> playersIds = new ArrayList<>();

    @Builder.Default
    private List<Long> tournamentsIds = new ArrayList<>();


}

