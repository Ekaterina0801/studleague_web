package com.studleague.studleague.dto.teamComposition;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.studleague.studleague.dto.player.PlayerMainInfoDTO;
import com.studleague.studleague.dto.team.TeamMainInfoDTO;
import com.studleague.studleague.dto.tournament.TournamentMainInfoDTO;
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

    private TeamMainInfoDTO parentTeam;

    private TournamentMainInfoDTO tournament;

    private List<PlayerMainInfoDTO> players;

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
