package com.studleague.studleague.dto.league;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.studleague.studleague.entities.League}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonIdentityReference()
@JsonIdentityInfo(scope = LeagueDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LeagueDTO {
    private Long id;
    private String name;
    private List<Long> tournamentsIds = new ArrayList<>();
    private List<Long> teamsIds = new ArrayList<>();
    private Integer countExcludedGames;
    private Long systemResultId;
    private Long createdById;
    private List<Long> managersIds = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeagueDTO leagueDTO = (LeagueDTO) o;
        return Objects.equals(name, leagueDTO.name) && Objects.equals(createdById, leagueDTO.createdById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, createdById);
    }
}