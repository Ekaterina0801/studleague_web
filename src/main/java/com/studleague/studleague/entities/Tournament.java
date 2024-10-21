package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tournaments")
@JsonIdentityInfo(scope= Tournament.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Tournament {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "id_site")
    @JsonProperty("idSite")
    private long idSite;

    @Column(name = "date_of_start")
    @JsonProperty("dateOfStart")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfStart;

    @Column(name = "date_of_final")
    @JsonProperty("dateOfEnd")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfEnd;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinTable(name="leagues_tournaments",
            joinColumns = @JoinColumn(name="tournament_id"), inverseJoinColumns=@JoinColumn(name="league_id"))
    @ToString.Exclude
    @Builder.Default
    private List<League> leagues = new ArrayList<>();

    @OneToMany(mappedBy = "tournament")
    //, cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}
    @ToString.Exclude
    @Builder.Default
    private List<FullResult> results = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "tournaments_teams",
            joinColumns = @JoinColumn(name = "tournament_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id")
    )
    @ToString.Exclude
    @Builder.Default
    private List<Team> teams = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "tournaments_players",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @ToString.Exclude
    @Builder.Default
    private List<Player> players = new ArrayList<>();

    public void addResult(FullResult fullResult) {
        if (!results.contains(fullResult)){
            results.add(fullResult);
            fullResult.setTournament(this);
        }
    }

    public void deleteResult(FullResult fullResult) {
        if (results != null) {
            results.remove(fullResult);
        }
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)){
            players.add(player);
            player.addTournamentToPlayer(this);
        }
    }

    public void deletePlayer(Player player) {
        if (players != null) {
            players.remove(player);
        }
    }

    public void addTeam(Team team) {
        if (!teams.contains(team)){
            teams.add(team);
            team.addTournamentToTeam(this);
        }
    }

    public void deleteTeam(Team team) {
        if (teams != null) {
            teams.remove(team);
        }
    }

    public void addLeague(League league){
        if (!leagues.contains(league))
        {
            leagues.add(league);
            league.getTournaments().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return id == that.id && idSite == that.idSite && Objects.equals(name, that.name) && Objects.equals(dateOfStart, that.dateOfStart) && Objects.equals(dateOfEnd, that.dateOfEnd) && Objects.equals(leagues, that.leagues) && Objects.equals(results, that.results) && Objects.equals(teams, that.teams) && Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, idSite, dateOfStart, dateOfEnd, leagues, results, teams, players);
    }
}
