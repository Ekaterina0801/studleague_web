package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamDTO {
    private int id;
    @JsonProperty("name")
    private String teamName;
    @JsonProperty("university")
    private String university;
    @JsonProperty("leagueId")
    private int leagueId;
    @JsonProperty("idSite")
    private String idSite;
    @JsonProperty("teamMembers")
    private List<PlayerDTO> players;

    public TeamDTO() {
    }

    public TeamDTO(int id, String teamName, String university, int leagueId, String idSite, List<PlayerDTO> players) {
        this.id = id;
        this.teamName = teamName;
        this.university = university;
        this.leagueId = leagueId;
        this.idSite = idSite;
        this.players = players;
    }

}

