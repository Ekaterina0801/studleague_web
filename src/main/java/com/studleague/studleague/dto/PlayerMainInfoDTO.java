package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.LocalDateDeserializer;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonIdentityReference()
public class PlayerMainInfoDTO {

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
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateOfBirth;

    @JsonProperty("idSite")
    private Long idSite;

}
