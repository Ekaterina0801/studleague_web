package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TeamDTO {
    private int id;
    @JsonProperty("teamName")
    private String teamName;
    @JsonProperty("university")
    private String university;
    @JsonProperty("leagueId")
    private int leagueId;

    public TeamDTO() {
    }

    public TeamDTO(int id, String teamName, String university, int leagueId) {
        this.id = id;
        this.teamName = teamName;
        this.university = university;
        this.leagueId = leagueId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

}

