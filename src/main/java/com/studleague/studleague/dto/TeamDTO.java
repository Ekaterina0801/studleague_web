package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityReference()
@JsonIdentityInfo(scope = TeamDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    private List<FlagDTO> flags = new ArrayList<>();

    @Builder.Default
    private List<TournamentDTO> tournaments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamDTO teamDTO = (TeamDTO) o;
        return Objects.equals(id, teamDTO.id) && Objects.equals(teamName, teamDTO.teamName) && Objects.equals(university, teamDTO.university) && Objects.equals(league, teamDTO.league) && Objects.equals(idSite, teamDTO.idSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamName, university, league, idSite);
    }
}

