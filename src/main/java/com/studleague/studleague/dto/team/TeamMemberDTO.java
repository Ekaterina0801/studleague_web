package com.studleague.studleague.dto.team;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.studleague.studleague.dto.player.PlayerDTO;
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

