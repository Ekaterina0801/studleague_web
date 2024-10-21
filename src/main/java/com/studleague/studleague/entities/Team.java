package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.*;
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
@ToString
@Table(name="teams")
@JsonIdentityInfo(scope=Team.class,generator = ObjectIdGenerators.PropertyGenerator.class , property = "id")
@AllArgsConstructor
public class Team {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="team_name")
    private String teamName;

    @Column(name="university")
    private String university;

    @Column(name="idSite")
    private long idSite;

    @ManyToOne()
    @JoinColumn(name="league_id",nullable = false)
    private League league;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name="teams_players",
    joinColumns = {@JoinColumn(name="team_id")}, inverseJoinColumns={@JoinColumn(name="player_id")})
    private List<Player> players = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name="teams_flags",
            joinColumns = @JoinColumn(name="team_id"), inverseJoinColumns=@JoinColumn(name="flag_id"))
    private List<Flag> flags = new ArrayList<>();

    @ManyToMany(mappedBy = "teams",cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    private List<Tournament> tournaments = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    @ToString.Exclude
    private List<FullResult> results = new ArrayList<>();


    public void addPlayerToTeam(Player player)
    {
        if (players==null)
        {
            players = new ArrayList<>();
        }
        if(!players.contains(player))
        {
            players.add(player);
            player.addTeamToPlayer(this);
        }


    }

    public void deletePlayerFromTeam(Player player)
    {
        if (players != null) {
            players.remove(player);
        }
    }


    public void addFlagToTeam(Flag flag)
    {
        if (flags==null)
        {
            flags = new ArrayList<>();
        }
        if (!flags.contains(flag))
        {
            flags.add(flag);
        }
    }

    public void deleteFlagFromTeam(Flag flag)
    {
        if (flags != null) {

            flags.remove(flag);
            flag.getTeams().remove(this);
        }
    }

    public void addTournamentToTeam(Tournament tournament)
    {
        if (tournaments==null)
        {
            tournaments = new ArrayList<>();
        }
        if (!tournaments.contains(tournament))
            tournaments.add(tournament);
    }

    public void deleteTournamentFromTeam(Tournament tournament)
    {
        if (tournaments != null) {
            tournaments.remove(tournament);
            tournament.getTeams().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id && idSite == team.idSite && Objects.equals(teamName, team.teamName) && Objects.equals(university, team.university) && Objects.equals(league, team.league) && Objects.equals(players, team.players) && Objects.equals(flags, team.flags) && Objects.equals(tournaments, team.tournaments) && Objects.equals(results, team.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamName, university, idSite, league, players, flags, tournaments, results);
    }
}
