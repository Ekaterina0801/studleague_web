package com.studleague.studleague.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TeamCompositionDTO {

    private Long id;

    private Long parentId;

    private Long tournamentId;

    private List<Long> playerIds;
}
