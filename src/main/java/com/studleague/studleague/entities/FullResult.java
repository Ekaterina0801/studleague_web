package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
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
@Table(name="full_results")
@JsonIdentityInfo(scope = FullResult.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FullResult {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="team_id")
    private Team team;

    @ManyToOne()
    //cascade={CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}
    @JoinColumn(name="tournament_id")
    @JsonBackReference
    private Tournament tournament;

    @Column(name="mask_results")
    private String mask_results;

    @OneToMany(mappedBy = "fullResult", cascade = CascadeType.MERGE,orphanRemoval=true)
    @ToString.Exclude
    @Builder.Default
    private List<Controversial> controversials = new ArrayList<>();

    public void addControversialToFullResult(Controversial controversial){
        if(!controversials.contains(controversial))
            controversials.add(controversial);
    }

    public void deleteControversialFromFullResult(Controversial controversial)
    {
        if (controversials != null) {
            controversials.remove(controversial);
            controversial.setFullResult(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullResult that = (FullResult) o;
        return Objects.equals(id, that.id) && Objects.equals(team, that.team) && Objects.equals(tournament, that.tournament) && Objects.equals(mask_results, that.mask_results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, team, tournament, mask_results);
    }
}
