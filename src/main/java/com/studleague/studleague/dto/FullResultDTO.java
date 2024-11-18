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

    @JsonProperty("team")
    private Long teamId;

    @JsonProperty("tournament")
    private Long tournamentId;

    @JsonProperty("maskResults")
    private String maskResults;

    private Integer totalScore;

    private List<ControversialDTO> controversials = new ArrayList<>();

}
