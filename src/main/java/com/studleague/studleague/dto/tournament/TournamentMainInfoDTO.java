package com.studleague.studleague.dto.tournament;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.LocalDateDeserializer;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIdentityReference()
public class TournamentMainInfoDTO {

    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("idSite")
    private Long idSite;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonProperty("dateStart")
    private LocalDateTime dateOfStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonProperty("dateEnd")
    private LocalDateTime dateOfEnd;


}
