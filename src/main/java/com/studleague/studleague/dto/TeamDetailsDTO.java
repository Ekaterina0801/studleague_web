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
    private long id;
    private TeamDTO team;
    private long leagueId;
    private long idSite;
    private String mask;
    private Integer questionsTotal;
    private String position;
    private List<Controversial> controversials = new ArrayList<>();
    private List<TeamMemberDTO> teamMembers = new ArrayList<>();

}

