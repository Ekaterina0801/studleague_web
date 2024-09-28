package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="flags")
@JsonIdentityInfo(scope = Flag.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @Override
    public String toString() {
        return "Flag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teams=" + teams +
                '}';
    }
}


