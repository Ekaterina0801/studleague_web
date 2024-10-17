package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="teams")
@JsonIdentityInfo(scope=Team.class,generator = ObjectIdGenerators.PropertyGenerator.class , property = "id")
public class Team {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="team_name")
    private String teamName;

    @Column(name="university")
    private String university;

    @Column(name="idSite")
    private String idSite;

    @ManyToOne()
    @JoinColumn(name="league_id",nullable = false)
    private League league;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name="teams_players",
    joinColumns = {@JoinColumn(name="team_id")}, inverseJoinColumns={@JoinColumn(name="player_id")})
    private List<Player> players = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name="teams_flags",
            joinColumns = @JoinColumn(name="team_id"), inverseJoinColumns=@JoinColumn(name="flag_id"))
    private List<Flag> flags = new ArrayList<>();

    @ManyToMany(mappedBy = "teams")
    private List<Tournament> tournaments = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<FullResult> results = new ArrayList<>();

    public Team(int id, String team_name, String university, String idSite) {
        this.id = id;
        this.teamName = team_name;
        this.university = university;
        this.flags = new ArrayList<>();
        this.idSite = idSite;
    }

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
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", teamName='" + teamName + '\'' +
                ", university='" + university + '\'' +
                ", idSite='" + idSite + '\'' +
                ", league=" + league +
                ", players=" + players +
                ", flags=" + flags +
                ", tournaments=" + tournaments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(teamName, team.teamName) && Objects.equals(university, team.university) && Objects.equals(idSite, team.idSite) && Objects.equals(league, team.league);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamName, university, idSite, league);
    }
}
