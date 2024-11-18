package com.studleague.studleague.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TeamCompositionDTO {

    private Long id;

    private TeamDTO parentTeam;

    private TournamentDTO tournament;

    private List<PlayerDTO> players;
}
