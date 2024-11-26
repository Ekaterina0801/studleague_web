package com.studleague.studleague.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name="team_compositions",uniqueConstraints = @UniqueConstraint(columnNames = {"tournament_id", "parent_team_id"}))
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
//@JsonIdentityInfo(scope=TeamComposition.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TeamComposition {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @NotNull
    private Team parentTeam;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @NotNull
    private Tournament tournament;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name="teamCompositions_players",
            joinColumns = {@JoinColumn(name="team_id")}, inverseJoinColumns={@JoinColumn(name="player_id")})
    @Builder.Default
    @ToString.Exclude
    private List<Player> players = new ArrayList<>();

    public void addPlayer(Player player)
    {
        if (!players.contains(player))
        {
            players.add(player);
        }
    }

    public void deletePlayer(Player player)
    {
        players.remove(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamComposition that = (TeamComposition) o;
        return Objects.equals(parentTeam, that.parentTeam) && Objects.equals(tournament, that.tournament);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentTeam, tournament);
    }
}
