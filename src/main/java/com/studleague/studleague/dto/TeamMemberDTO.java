package com.studleague.studleague.dto;

import com.studleague.studleague.entities.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamMemberDTO {
    private int id;
    private String flag;
    private Integer usedRating;
    private Integer rating;
    private PlayerDTO player;


}

