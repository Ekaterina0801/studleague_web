package com.studleague.studleague.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberDTO {
    private Long id;
    private String flag;
    private Integer usedRating;
    private Integer rating;
    private PlayerDTO player;
}

