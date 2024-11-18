package com.studleague.studleague.dto;

import com.studleague.studleague.entities.Controversial;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TeamDetailsDTO {
    private Long id;
    private TeamDTO team;
    private Long leagueId;
    private Long idSite;
    private String mask;
    private Integer questionsTotal;
    private String position;
    private List<ControversialDTO> controversials = new ArrayList<>();
    private List<TeamMemberDTO> teamMembers = new ArrayList<>();

}

