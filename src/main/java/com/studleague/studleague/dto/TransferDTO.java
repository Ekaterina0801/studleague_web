package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.CustomJsonDateDeserializer;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(scope = TransferDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TransferDTO {

    @Id
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferDTO that = (TransferDTO) o;
        return Objects.equals(transferDate, that.transferDate) && Objects.equals(player, that.player) && Objects.equals(oldTeam, that.oldTeam) && Objects.equals(newTeam, that.newTeam) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferDate, player, oldTeam, newTeam, comments);
    }
}
