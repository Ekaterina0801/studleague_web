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
public class FlagDto {
    long id;
    String name;
    List<Long> teamIds;
}