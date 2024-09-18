package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class FullResultDTO {

    private int id;

    @JsonProperty("teamId")
    private int team_id;

    @JsonProperty("tournamentId")
    private int tournament_id;

    @JsonProperty("maskResults")
    private String mask_results;

    private List<ControversialDTO> controversials = new ArrayList<>();

    public FullResultDTO() {
    }

    public FullResultDTO(int id, int team_id, int tournament_id, String mask_results, List<ControversialDTO> controversials) {
        this.id = id;
        this.team_id = team_id;
        this.tournament_id = tournament_id;
        this.mask_results = mask_results;
        this.controversials = controversials;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public int getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(int tournament_id) {
        this.tournament_id = tournament_id;
    }

    public String getMask_results() {
        return mask_results;
    }

    public void setMask_results(String mask_results) {
        this.mask_results = mask_results;
    }

    public List<ControversialDTO> getControversials() {
        return controversials;
    }

    public void setControversials(List<ControversialDTO> controversials) {
        this.controversials = controversials;
    }
}
