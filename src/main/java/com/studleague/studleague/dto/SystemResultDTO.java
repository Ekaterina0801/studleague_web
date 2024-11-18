package com.studleague.studleague.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.studleague.studleague.entities.SystemResult}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SystemResultDTO {
    Long id;
    @NotBlank
    String name;

    @NotBlank
    String description;

    @Min(message = "Value must not be negative", value = 0)
    Integer countNotIncludedGames;

    @Builder.Default
    List<Long> leaguesIds = new ArrayList<>();
}