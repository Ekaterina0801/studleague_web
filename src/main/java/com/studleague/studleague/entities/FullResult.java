package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="fullResults")
@JsonIdentityInfo(scope = League.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FullResult {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name="team_id",nullable = false)
    private Team team;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name="tournament_id",nullable = false)
    @JsonBackReference
    private Tournament tournament;

    @Column(name="mask_results")
    private String mask_results;

    @OneToMany(mappedBy = "fullResult", cascade = CascadeType.ALL,orphanRemoval=true)
    private List<Controversial> controversials = new ArrayList<>();

    public FullResult() {
    }

    public FullResult(int id, Team team, Tournament tournament, String mask_results) {
        this.id = id;
        this.team = team;
        this.tournament = tournament;
        this.mask_results = mask_results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void addControversialToFullResult(Controversial controversial){
        if (controversials==null)
        {
            controversials = new ArrayList<>();
        }
        if(!controversials.contains(controversial))
            controversials.add(controversial);
    }

    public void deleteControversialFromFullResult(Controversial controversial)
    {
        if (controversials != null) {
            controversials.remove(controversial);
            controversial.setFullResult(null);
        }
    }
    public String getMask_results() {
        return mask_results;
    }

    public void setMask_results(String mask_results) {
        this.mask_results = mask_results;
    }

    public List<Controversial> getControversials() {
        return controversials;
    }

    public void setControversials(List<Controversial> controversials) {
        this.controversials = controversials;
    }

}
