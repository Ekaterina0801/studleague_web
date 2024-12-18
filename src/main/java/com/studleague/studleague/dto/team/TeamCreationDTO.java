package com.studleague.studleague.dto.team;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIdentityInfo(scope = TeamDetailsDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TeamCreationDTO {

    @JsonProperty("name")
    private String teamName;

    private String university;

    private Long leagueId;

    private List<Long> tournamentIds;

    private Long idSite;
}
