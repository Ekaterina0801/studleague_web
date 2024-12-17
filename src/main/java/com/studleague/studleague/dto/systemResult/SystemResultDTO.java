package com.studleague.studleague.dto.systemResult;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.studleague.studleague.entities.SystemResult}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonIdentityReference()
@JsonIdentityInfo(scope = SystemResultDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemResultDTO that = (SystemResultDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(countNotIncludedGames, that.countNotIncludedGames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, countNotIncludedGames);
    }
}