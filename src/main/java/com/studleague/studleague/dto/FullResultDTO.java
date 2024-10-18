package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
