package com.studleague.studleague.entities;



import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@JsonIdentityInfo(scope = League.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name="leagues")
public class League {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @ManyToMany(mappedBy = "leagues", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Tournament> tournaments = new ArrayList<>();


    @OneToMany(mappedBy = "league", cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    //@JsonManagedReference
    private List<Team> teams = new ArrayList<>();

    public League(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addTournamentToLeague(Tournament tournament){
        if (tournaments==null)
        {
            tournaments = new ArrayList<>();
        }
        if(!tournaments.contains(tournament))
        {
            tournaments.add(tournament);
            tournament.getLeagues().add(this);
            //tournament.addLeague(this);
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
    public String toString() {
        return "League{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
