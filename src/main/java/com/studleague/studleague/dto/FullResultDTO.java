package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIdentityInfo(scope = FullResultDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FullResultDTO {

    private Long id;

    @JsonProperty("team")
    private TeamMainInfoDTO team;

    @JsonProperty("tournament")
    private Long tournamentId;

    @JsonProperty("maskResults")
    private String maskResults;

    private Double totalScore;

    private List<ControversialDTO> controversials = new ArrayList<>();

}
