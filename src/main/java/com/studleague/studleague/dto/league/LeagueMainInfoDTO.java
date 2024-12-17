package com.studleague.studleague.dto.league;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonIdentityReference()
public class LeagueMainInfoDTO {

    private Long id;
    private String name;
    private Integer countExcludedGames;
    private Long systemResultId;
    private Long createdById;
}
