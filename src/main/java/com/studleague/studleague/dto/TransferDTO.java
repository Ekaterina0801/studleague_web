package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.CustomJsonDateDeserializer;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferDTO {
    private long id;
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate transferDate;
    @JsonProperty("playerId")
    private long playerId;
    @JsonProperty("oldTeamId")
    private long oldTeamId;
    @JsonProperty("newTeamId")
    private long newTeamId;
    private String comments;
}
