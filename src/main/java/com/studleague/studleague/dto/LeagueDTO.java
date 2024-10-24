package com.studleague.studleague.dto;

import lombok.*;

import java.util.List;

/**
 * DTO for {@link com.studleague.studleague.entities.League}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LeagueDTO {
    private long id;
    private String name;
    private List<Long> tournamentIds;
    private List<Long> teamIds;
}