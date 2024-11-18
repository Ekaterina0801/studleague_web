package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.CustomJsonDateDeserializer;
import com.studleague.studleague.entities.Player;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferDTO {
    private Long id;
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate transferDate;
    @JsonProperty("playerId")
    private PlayerDTO player;
    @JsonProperty("oldTeamId")
    private TeamDTO oldTeam;
    @JsonProperty("newTeamId")
    private TeamDTO newTeam;
    private String comments;
}
