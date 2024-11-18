package com.studleague.studleague.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.LocalDateDeserializer;
import com.studleague.studleague.entities.TeamComposition;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDate;
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
public class TournamentDTO {
    private Long id;
    @JsonProperty("name")
    private String name;

    @JsonProperty("idSite")
    private Long idSite;

    @Temporal(TemporalType.DATE)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonProperty("dateStart")
    private LocalDateTime dateOfStart;

    @Temporal(TemporalType.DATE)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonProperty("dateEnd")
    private LocalDateTime dateOfEnd;

    @Builder.Default
    private List<Long> leaguesIds = new ArrayList<>();

    @Builder.Default
    private List<Long> playerIds = new ArrayList<>();

    @Builder.Default
    private List<Long> teamIds = new ArrayList<>();

    private List<TeamComposition> teamCompositions = new ArrayList<>();

    private List<FullResultDTO> results = new ArrayList<>();


}
