package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
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
@Table(name="players")
@JsonIdentityInfo(scope= Player.class,generator = ObjectIdGenerators.PropertyGenerator.class , property = "id")
public class Player {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="patronymic")
    private String patronymic;

    @Column(name="surname")
    private String surname;

    @Column(name="university")
    private String university;

    @Column(name="date_of_birth")
    private String dateOfBirth;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            mappedBy = "players")
    //@JsonIgnore

    private List<Team> teams = new ArrayList<>();

    @ManyToMany(mappedBy = "players")
    //@JsonBackReference
    //@JsonIgnore
    //@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class , property = "id")
    private List<Tournament> tournaments = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Transfer> transfers = new ArrayList<>();

    public Player(int id, String name, String patronymic, String surname, String university, String dateOfBirth, List<Tournament> tournaments, List<Transfer> transfers, List<Team> teams) {
        this.id = id;
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.university = university;
        this.dateOfBirth = dateOfBirth;
        this.tournaments = tournaments;
        this.transfers = transfers;
        this.teams = teams;
    }

    public Player(int id, String name, String patronymic, String surname, String university, String date_of_birth) {
        this.id = id;
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.university = university;
        this.dateOfBirth = date_of_birth;
    }

    public void addTeamToPlayer(Team team)
    {
        if (teams == null) {
            teams = new ArrayList<>();
          }
                if (!teams.contains(team)) {
                  teams.add(team);
             }
    }

    public void addTournamentToPlayer(Tournament tournament)
    {
        if (tournaments == null) {
            tournaments = new ArrayList<>();
        }
        if (!tournaments.contains(tournament)) {
            tournaments.add(tournament);
        }
    }

}
