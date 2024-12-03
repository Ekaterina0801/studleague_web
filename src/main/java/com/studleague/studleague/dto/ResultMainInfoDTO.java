package com.studleague.studleague.dto;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
