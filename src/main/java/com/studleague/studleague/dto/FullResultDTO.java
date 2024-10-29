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

    private Long id;

    @JsonProperty("teamId")
    private Long team_id;

    @JsonProperty("tournamentId")
    private Long tournament_id;

    @JsonProperty("maskResults")
    private String mask_results;

    private List<ControversialDTO> controversials = new ArrayList<>();

}
