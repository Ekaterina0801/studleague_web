package com.studleague.studleague.dto.tournament;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.LocalDateTimeDeserializer;
import com.studleague.studleague.dto.result.FullResultDTO;
import com.studleague.studleague.dto.teamComposition.TeamCompositionDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIdentityReference()
@JsonIdentityInfo(scope = TournamentDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TournamentDTO {
    private Long id;
    @JsonProperty("name")
    private String name;

    @JsonProperty("idSite")
    private Long idSite;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("dateStart")
    private LocalDateTime dateOfStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("dateEnd")
    private LocalDateTime dateOfEnd;

    @Builder.Default
    private List<Long> leaguesIds = new ArrayList<>();

    @Builder.Default
    private List<TeamCompositionDTO> teamCompositions = new ArrayList<>();

    @Builder.Default
    private List<FullResultDTO> results = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TournamentDTO that = (TournamentDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(idSite, that.idSite) && Objects.equals(dateOfStart, that.dateOfStart) && Objects.equals(dateOfEnd, that.dateOfEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, idSite, dateOfStart, dateOfEnd);
    }
}
