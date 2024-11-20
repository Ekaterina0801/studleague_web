package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(scope = TeamMemberDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TeamMemberDTO {
    private Long id;
    private String flag;
    private Integer usedRating;
    private Integer rating;
    private PlayerDTO player;
}

