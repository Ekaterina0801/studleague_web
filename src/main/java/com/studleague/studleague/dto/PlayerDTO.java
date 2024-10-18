package com.studleague.studleague.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.studleague.studleague.entities.Team;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PlayerDTO {
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("patronymic")
    private String patronymic;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("university")
    private String university;
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;;
    @JsonProperty("idSite")
    private long idSite;
    @JsonProperty("teams")
    private List<Long> teamIds = new ArrayList<>();

}

