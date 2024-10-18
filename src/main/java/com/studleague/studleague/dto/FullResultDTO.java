package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FullResultDTO {

    private long id;

    @JsonProperty("teamId")
    private long team_id;

    @JsonProperty("tournamentId")
    private long tournament_id;

    @JsonProperty("maskResults")
    private String mask_results;

    private List<ControversialDTO> controversials = new ArrayList<>();

}
