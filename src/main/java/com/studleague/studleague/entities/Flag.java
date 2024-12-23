package com.studleague.studleague.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@AllArgsConstructor
public class Flag {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    @NotBlank
    private String name;

    @ManyToMany(mappedBy = "flags", cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST})
    @ToString.Exclude
    @Builder.Default
    private List<Team> teams = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "league_id", nullable = false)
    private League league;


    public void addTeamToFlag(Team team)
    {
        if (!teams.contains(team)) {
            teams.add(team);
            team.addFlagToTeam(this);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flag flag = (Flag) o;
        return Objects.equals(id, flag.id) && Objects.equals(name, flag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}


