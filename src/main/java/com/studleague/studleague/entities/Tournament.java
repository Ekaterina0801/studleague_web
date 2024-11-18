package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "idSite")
    @JsonProperty("idSite")
    private Long idSite;

    @Column(name = "dateOfStart")
    @JsonProperty("dateOfStart")
    @ColumnDefault("'2000-01-01 10:23:54'::timestamp without time zone")
    private LocalDateTime dateOfStart;

    @Column(name = "dateOfFinal")
    @JsonProperty("dateOfEnd")
    @ColumnDefault("'2000-01-01 10:23:54'::timestamp without time zone")
    private LocalDateTime dateOfEnd;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinTable(name="leagues_tournaments",
            joinColumns = @JoinColumn(name="tournament_id"), inverseJoinColumns=@JoinColumn(name="league_id"))
    @ToString.Exclude
    @Builder.Default
    private List<League> leagues = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
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

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "tournaments_players",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @ToString.Exclude
    @Builder.Default
    private List<Player> players = new ArrayList<>();

    @OneToMany(mappedBy = "tournament",cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<TeamComposition> teamCompositions = new ArrayList<>();



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
            team.getTournaments().remove(this);
        }
    }

    public void addLeague(League league){
        if (!leagues.contains(league))
        {
            leagues.add(league);
            league.getTournaments().add(this);
        }
    }

    public void addTeamComposition(TeamComposition teamComposition){
        if (!teamCompositions.contains(teamComposition))
        {
            teamCompositions.add(teamComposition);
            teamComposition.setTournament(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return Objects.equals(id, that.id) && Objects.equals(idSite, that.idSite) && Objects.equals(name, that.name) && Objects.equals(dateOfStart, that.dateOfStart) && Objects.equals(dateOfEnd, that.dateOfEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, idSite, dateOfStart, dateOfEnd);
    }
}
