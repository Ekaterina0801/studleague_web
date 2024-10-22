package com.studleague.studleague.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TournamentDto {
    private long id;
    @JsonProperty("name")
    private String name;

    @JsonProperty("idSite")
    private long idSite;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonProperty("dateStart")
    private LocalDate dateOfStart;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonProperty("dateEnd")
    private LocalDate dateOfEnd;

    private List<Long> leagueIds = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TournamentDto entity = (TournamentDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.idSite, entity.idSite) &&
                Objects.equals(this.dateOfStart, entity.dateOfStart) &&
                Objects.equals(this.dateOfEnd, entity.dateOfEnd) &&
                Objects.equals(this.leagueIds, entity.leagueIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, idSite, dateOfStart, dateOfEnd, leagueIds);
    }

}
