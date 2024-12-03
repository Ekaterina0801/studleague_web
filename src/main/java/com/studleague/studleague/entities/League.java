package com.studleague.studleague.entities;

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
@Table(name="leagues")
//@JsonIdentityInfo(scope=League.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class League {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    @NotBlank
    private String name;

    @Column(name = "countExcludedGames")
    private Integer countExcludedGames;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User createdBy;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "leagues", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @Builder.Default
    private List<User> managers = new ArrayList<>();

    @ManyToMany(mappedBy = "leagues", fetch = FetchType.LAZY, cascade={CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @Builder.Default
    private List<Tournament> tournaments = new ArrayList<>();


    @OneToMany(mappedBy = "league", cascade={CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @Builder.Default
    private List<Team> teams = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name="system_result_id")
    @ToString.Exclude
    private SystemResult systemResult;


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
