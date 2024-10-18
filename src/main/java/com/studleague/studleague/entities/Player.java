package com.studleague.studleague.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="players")
@Builder
@ToString
@AllArgsConstructor
@JsonIdentityInfo(scope= Player.class,generator = ObjectIdGenerators.PropertyGenerator.class , property = "id")
public class Player {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @Column(name="id_site")
    private long idSite;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            mappedBy = "players")
    @ToString.Exclude
    private List<Team> teams = new ArrayList<>();

    @ManyToMany(mappedBy = "players", cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @ToString.Exclude
    private List<Tournament> tournaments = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @ToString.Exclude
    private List<Transfer> transfers = new ArrayList<>();

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

    public String getFullName()
    {
        return this.name+" "+this.patronymic+" "+this.surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) && Objects.equals(patronymic, player.patronymic) && Objects.equals(surname, player.surname) && Objects.equals(idSite, player.idSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, patronymic, surname, idSite);
    }
}
