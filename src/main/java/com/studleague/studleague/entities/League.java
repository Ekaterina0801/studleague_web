package com.studleague.studleague.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@JsonIdentityInfo(scope = League.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name="leagues")
public class League {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @ManyToMany(mappedBy = "leagues", fetch = FetchType.LAZY, cascade={CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    private List<Tournament> tournaments = new ArrayList<>();


    @OneToMany(mappedBy = "league", cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @ToString.Exclude
    private List<Team> teams = new ArrayList<>();


    public void addTournamentToLeague(Tournament tournament){
        if (tournaments==null)
        {
            tournaments = new ArrayList<>();
        }
        if(!tournaments.contains(tournament))
        {
            tournaments.add(tournament);
            tournament.getLeagues().add(this);
        }


    }

    public void deleteTournamentFromLeague(Tournament tournament)
    {
        if (tournaments != null) {
            tournaments.remove(tournament);
            tournament.getLeagues().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return id == league.id && Objects.equals(name, league.name) && Objects.equals(tournaments, league.tournaments) && Objects.equals(teams, league.teams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tournaments, teams);
    }
}
