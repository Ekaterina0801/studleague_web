package com.studleague.studleague.dto.transfer;


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
@JsonIdentityInfo(scope = TransferCreationDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TransferCreationDTO {

    @Id
    private Long id;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transferDate;

    @JsonProperty("playerId")
    private Long playerId;

    @JsonProperty("oldTeamId")
    private Long oldTeamId;

    @JsonProperty("newTeamId")
    private Long newTeamId;

    private String comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferCreationDTO that = (TransferCreationDTO) o;
        return Objects.equals(transferDate, that.transferDate) && Objects.equals(playerId, that.playerId) && Objects.equals(oldTeamId, that.oldTeamId) && Objects.equals(newTeamId, that.newTeamId) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferDate, playerId, oldTeamId, newTeamId, comments);
    }
}

