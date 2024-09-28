package com.studleague.studleague.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TournamentDto {
    private int id;
    private String name;
    private String idSite;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date dateOfStart;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date dateOfEnd;
    private List<Integer> leagueIds;

    public TournamentDto() {
    }

    public TournamentDto(int id, String name, String idSite, Date dateOfStart, Date dateOfEnd, List<Integer> leagueIds) {
        this.id = id;
        this.name = name;
        this.idSite = idSite;
        this.dateOfStart = dateOfStart;
        this.dateOfEnd = dateOfEnd;
        this.leagueIds = leagueIds;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdSite() {
        return idSite;
    }


    public Date getDateOfStart() {
        return dateOfStart;
    }

    public Date getDateOfEnd() {
        return dateOfEnd;
    }

    public List<Integer> getLeagueIds() {
        return leagueIds;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdSite(String idSite) {
        this.idSite = idSite;
    }

    public void setDateOfStart(Date dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public void setDateOfEnd(Date dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public void setLeagueIds(List<Integer> leagueIds) {
        this.leagueIds = leagueIds;
    }

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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "idSite = " + idSite + ", " +
                "dateOfStart = " + dateOfStart + ", " +
                "dateOfEnd = " + dateOfEnd + ", " +
                "leagueIds = " + leagueIds + ")";
    }
}
