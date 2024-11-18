package com.studleague.studleague.dto;

import com.studleague.studleague.entities.security.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.studleague.studleague.entities.League}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LeagueDTO {
    private Long id;
    private String name;
    private List<Long> tournamentsIds = new ArrayList<>();
    private List<Long> teamsIds = new ArrayList<>();
    private Long systemResultId;
    private Long createdById;
    private List<Long> managersIds = new ArrayList<>();


}