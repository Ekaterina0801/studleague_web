package com.studleague.studleague.dto.league;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.studleague.studleague.dto.security.UserMainInfoDTO;
import lombok.*;

import java.util.List;

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
    private List<UserMainInfoDTO> managers;
}
