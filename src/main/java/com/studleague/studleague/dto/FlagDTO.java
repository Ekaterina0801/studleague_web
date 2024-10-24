package com.studleague.studleague.dto;

import lombok.*;

import java.util.List;

/**
 * DTO for {@link com.studleague.studleague.entities.Flag}
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FlagDTO {
    private long id;
    private String name;
    private List<Long> teamIds;
}