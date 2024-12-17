package com.studleague.studleague.dto.player;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.LocalDateDeserializer;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonIdentityReference()
public class PlayerCreationDTO {

    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("patronymic")
    private String patronymic;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("university")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String university;

    @JsonProperty("dateOfBirth")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateOfBirth;

    @JsonProperty("idSite")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long idSite;

    @JsonProperty("teamIds")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Long> teamIds = new ArrayList<>();

}
