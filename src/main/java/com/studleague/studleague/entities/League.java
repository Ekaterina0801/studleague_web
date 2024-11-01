package com.studleague.studleague.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.studleague.studleague.entities.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIdentityInfo(scope = League.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name="leagues")
public class League {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_leagues_user"))
    @NotNull
    private User createdBy;

    @ManyToMany(mappedBy = "leagues", cascade={CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @Builder.Default
    private List<User> managers = new ArrayList<>();

    @ManyToMany(mappedBy = "leagues", fetch = FetchType.LAZY, cascade={CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @Builder.Default
    private List<Tournament> tournaments = new ArrayList<>();


    @OneToMany(mappedBy = "league", cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @Builder.Default
    private List<Team> teams = new ArrayList<>();


    public void addTournamentToLeague(Tournament tournament){
        if(!tournaments.contains(tournament))
        {
            tournaments.add(tournament);
            tournament.getLeagues().add(this);
        }
    }

    public void deleteTournamentFromLeague(Tournament tournament)
    {
        if (tournaments != null) {
            tournaments.remove(tournament);
            tournament.getLeagues().remove(this);
        }
    }

    public void addManager(User user)
    {
        if (!managers.contains(user))
        {
            managers.add(user);
            user.getLeagues().add(this);
        }
    }

    public void deleteManager(User user)
    {
        if (managers!=null)
        {
            managers.remove(user);
            user.getLeagues().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return Objects.equals(id, league.id) && Objects.equals(name, league.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
