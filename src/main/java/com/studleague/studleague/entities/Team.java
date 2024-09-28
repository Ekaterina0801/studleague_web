package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.*;
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

    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
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

    public Team(int id, String team_name, String university) {
        this.id = id;
        this.teamName = team_name;
        this.university = university;
        this.flags = new ArrayList<>();
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
               player.getTeams().remove(this);
        }
    }


    public void addFlagToTeam(Flag flag)
    {
        if (flags==null)
        {
            flags = new ArrayList<>();
        }
        flags.add(flag);
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
                ", team_name='" + teamName + '\'' +
                ", university='" + university + '\'' +
                ", players=" + players +
                ", flags=" + flags +
                '}';
    }
}
