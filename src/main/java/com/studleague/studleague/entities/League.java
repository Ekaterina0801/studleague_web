package com.studleague.studleague.entities;



import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(scope = League.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name="leagues")
public class League {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @ManyToMany(cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name="leagues_tournaments",
            joinColumns = @JoinColumn(name="league_id"), inverseJoinColumns=@JoinColumn(name="tournament_id"))
    //@JsonManagedReference(value="tournaments")


    private List<Tournament> tournaments = new ArrayList<>();

    @OneToMany(mappedBy = "league", cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    //@JsonManagedReference
    private List<Team> teams = new ArrayList<>();

    public League() {
    }

    public League(int id, String name) {
        this.id = id;
        this.name = name;
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

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public void addTournamentToLeague(Tournament tournament){
        if (tournaments==null)
        {
            tournaments = new ArrayList<>();
        }
        if(!tournaments.contains(tournament))
            tournaments.add(tournament);
    }

    public void deleteTournamentFromLeague(Tournament tournament)
    {
        if (tournaments != null) {
            tournaments.remove(tournament);
            tournament.getLeagues().remove(this);
        }
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "League{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
