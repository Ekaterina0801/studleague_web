package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonIdentityReference()
@JsonIdentityInfo(scope = TeamCompositionDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TeamCompositionDTO {

    private Long id;

    private TeamDTO parentTeam;

    private TournamentDTO tournament;

    private List<PlayerDTO> players;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamCompositionDTO that = (TeamCompositionDTO) o;
        return Objects.equals(parentTeam, that.parentTeam) && Objects.equals(tournament, that.tournament);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentTeam, tournament);
    }
}
