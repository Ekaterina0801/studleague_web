package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDTO {
    private long id;
    @JsonProperty("name")
    private String teamName;
    @JsonProperty("university")
    private String university;
    @JsonProperty("leagueId")
    private long leagueId;
    @JsonProperty("idSite")
    private long idSite;
    @JsonProperty("teamMembers")
    private List<PlayerDTO> players = new ArrayList<>();


}

