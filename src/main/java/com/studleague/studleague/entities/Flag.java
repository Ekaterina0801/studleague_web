package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name="flags")
@Builder
@NoArgsConstructor
@ToString
@JsonIdentityInfo(scope = Flag.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@AllArgsConstructor
public class Flag {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @ManyToMany(mappedBy = "flags", cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @ToString.Exclude
    @Builder.Default
    private List<Team> teams = new ArrayList<>();


    public void addTeamToFlag(Team team)
    {
        if (!teams.contains(team))
            teams.add(team);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flag flag = (Flag) o;
        return id == flag.id && Objects.equals(name, flag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}


