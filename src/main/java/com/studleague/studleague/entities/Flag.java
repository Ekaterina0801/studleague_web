package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="flags")
public class Flag {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @ManyToMany(mappedBy = "flags")
    //@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class , property = "id")
    private List<Team> teams = new ArrayList<>();

    public Flag() {
    }


    public Flag(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addTeamToFlag(Team team)
    {
        if (teams==null)
        {
            teams = new ArrayList<>();
        }
        teams.add(team);
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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "Flag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teams=" + teams +
                '}';
    }
}


