package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIdentityInfo(scope = TeamDetailsDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

