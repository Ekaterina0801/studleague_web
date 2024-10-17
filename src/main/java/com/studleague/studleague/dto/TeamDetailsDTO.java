package com.studleague.studleague.dto;

import com.studleague.studleague.entities.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeamDetailsDTO {
    private int id;
    private TeamDTO team;
    private int leagueId;
    private int idSite;
    private String mask;
    private Integer questionsTotal;
    private String position;
    private List<Object> controversials; // Assuming controversials is a list of objects
    private List<TeamMemberDTO> teamMembers;

}

