package com.studleague.studleague.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@Setter
@Table(name = "players")
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(scope = Player.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Player {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "surname")
    @NotBlank
    private String surname;

    @Column(name = "university")
    private String university;

    @Column(name = "date_of_birth")
    @Past
    private LocalDate dateOfBirth;

    @Column(name = "id_site")
    private Long idSite;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            mappedBy = "players")
    @ToString.Exclude
    @Builder.Default
    private List<Team> teams = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            mappedBy = "players")
    @ToString.Exclude
    @Builder.Default
    private List<TeamComposition> teamsCompositions = new ArrayList<>();

    @ManyToMany(mappedBy = "players", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH,})
    @ToString.Exclude
    @Builder.Default
    private List<Tournament> tournaments = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @Builder.Default
    private List<Transfer> transfers = new ArrayList<>();


    public void addTeamToPlayer(Team team) {

        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void addTournamentToPlayer(Tournament tournament) {
        if (tournaments == null) {
            tournaments = new ArrayList<>();
        }
        if (!tournaments.contains(tournament)) {
            tournaments.add(tournament);
        }
    }

    public String getFullName() {
        return this.name + " " + this.patronymic + " " + this.surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) && Objects.equals(idSite, player.idSite) && Objects.equals(name, player.name) && Objects.equals(patronymic, player.patronymic) && Objects.equals(surname, player.surname) && Objects.equals(university, player.university) && Objects.equals(dateOfBirth, player.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, patronymic, surname, university, dateOfBirth, idSite);
    }
}
