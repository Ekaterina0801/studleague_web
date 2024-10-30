package com.studleague.studleague.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="team_compositions")
@Builder
@NoArgsConstructor
@ToString
@JsonIdentityInfo(scope = TeamComposition.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@AllArgsConstructor
public class TeamComposition {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @NotNull
    private Team parentTeam;

    @ManyToOne
    @NotNull
    private Tournament tournament;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
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
}
