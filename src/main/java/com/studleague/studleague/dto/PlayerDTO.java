package com.studleague.studleague.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.studleague.studleague.entities.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlayerDTO {
    private int id;
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
    private String idSite;
    @JsonProperty("teams")
    private List<Integer> teamIds;

    public PlayerDTO(int id, String name, String patronymic, String surname, String university, String dateOfBirth, String idSite, List<Integer> teamIds) {
        this.id = id;
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.university = university;
        this.dateOfBirth = dateOfBirth;
        this.teamIds = teamIds;
        this.idSite = idSite;
    }


}

