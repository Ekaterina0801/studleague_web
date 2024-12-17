package com.studleague.studleague.dto.player;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.LocalDateDeserializer;
import com.studleague.studleague.dto.team.TeamMainInfoDTO;
import com.studleague.studleague.dto.teamComposition.TeamCompositionDTO;
import com.studleague.studleague.dto.transfer.TransferMainInfoDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonIdentityReference()
@JsonIdentityInfo(scope = PlayerDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PlayerDTO {
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("patronymic")
    private String patronymic;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("university")
    private String university;

    @JsonProperty("dateOfBirth")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateOfBirth;

    @JsonProperty("idSite")
    private Long idSite;

    @JsonProperty("teams")
    private List<TeamMainInfoDTO> teams = new ArrayList<>();

    @JsonProperty("teamCompositions")
    private List<TeamCompositionDTO> teamsCompositions = new ArrayList<>();

    @JsonProperty("transfers")
    private List<TransferMainInfoDTO> transfers = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDTO playerDTO = (PlayerDTO) o;
        return Objects.equals(id, playerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

