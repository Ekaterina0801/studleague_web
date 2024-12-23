package com.studleague.studleague.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@Table(name="teams")
//@JsonIdentityInfo(scope=Team.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@AllArgsConstructor
public class Team {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="teamName")
    @NotBlank
    private String teamName;

    @Column(name="university")
    private String university;

    @Column(name="idSite",unique = true)
    private Long idSite;

    @ManyToOne()
    @JoinColumn(name="leagueId")
    private League league;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name="teams_players",
    joinColumns = {@JoinColumn(name="team_id")}, inverseJoinColumns={@JoinColumn(name="player_id")})
    @Builder.Default
    @ToString.Exclude
    private List<Player> players = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name="teams_flags",
            joinColumns = @JoinColumn(name="team_id"), inverseJoinColumns=@JoinColumn(name="flag_id"))
    @Builder.Default
    @ToString.Exclude
    private List<Flag> flags = new ArrayList<>();

    @ManyToMany(mappedBy = "teams", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    @Builder.Default
    private List<Tournament> tournaments = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<FullResult> results = new ArrayList<>();

    @OneToMany(mappedBy = "parentTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<TeamComposition> teamCompositions = new ArrayList<>();


    public void addPlayerToTeam(Player player) {
        if (!players.contains(player)) {
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
        return Objects.equals(id, team.id) && Objects.equals(teamName, team.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamName);
    }
}
