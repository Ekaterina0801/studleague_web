package com.studleague.studleague.dto.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.CustomJsonDateDeserializer;
import com.studleague.studleague.dto.player.PlayerMainInfoDTO;
import com.studleague.studleague.dto.team.TeamMainInfoDTO;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(scope = TransferMainInfoDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TransferMainInfoDTO {

    @Id
    private Long id;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transferDate;

    @JsonProperty("playerId")
    private PlayerMainInfoDTO player;

    @JsonProperty("oldTeam")
    private TeamMainInfoDTO oldTeam;

    @JsonProperty("newTeam")
    private TeamMainInfoDTO newTeam;

    private String comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferMainInfoDTO that = (TransferMainInfoDTO) o;
        return Objects.equals(transferDate, that.transferDate) && Objects.equals(player, that.player) && Objects.equals(oldTeam, that.oldTeam) && Objects.equals(newTeam, that.newTeam) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferDate, player, oldTeam, newTeam, comments);
    }
}