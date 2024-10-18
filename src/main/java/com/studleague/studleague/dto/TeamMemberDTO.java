package com.studleague.studleague.dto;

import com.studleague.studleague.entities.Player;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberDTO {
    private int id;
    private String flag;
    private Integer usedRating;
    private Integer rating;
    private PlayerDTO player;
}

