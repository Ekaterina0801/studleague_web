package com.studleague.studleague.dto.result;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.studleague.studleague.dto.team.TeamMainInfoDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIdentityInfo(scope = ResultMainInfoDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ResultMainInfoDTO {
    private Long id;

    @JsonProperty("team")
    private TeamMainInfoDTO team;
}
