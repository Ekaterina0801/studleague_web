package com.studleague.studleague.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.studleague.studleague.entities.Team;

import java.util.List;

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
    @JsonProperty("teams")
    private List<Integer> teamIds;

    public PlayerDTO(int id, String name, String patronymic, String surname, String university, String dateOfBirth, List<Integer> teamIds) {
        this.id = id;
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.university = university;
        this.dateOfBirth = dateOfBirth;
        this.teamIds = teamIds;
    }

    public PlayerDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Integer> getTeamId() {
        return teamIds;
    }

    public void setTeamIds(List<Integer> teamIds) {
        this.teamIds = teamIds;
    }
}

