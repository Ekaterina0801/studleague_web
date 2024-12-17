package com.studleague.studleague.dto.flag;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.studleague.studleague.entities.Flag}
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonIdentityInfo(scope = FlagDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FlagDTO {
    private Long id;
    private String name;
    private List<Long> teamsIds;
    private Long leagueId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlagDTO flagDTO = (FlagDTO) o;
        return Objects.equals(id, flagDTO.id) && Objects.equals(name, flagDTO.name) && Objects.equals(leagueId, flagDTO.leagueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, leagueId);
    }
}