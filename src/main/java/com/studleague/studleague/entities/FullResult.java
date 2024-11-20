package com.studleague.studleague.entities;


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
//@JsonIdentityInfo(scope=FullResult.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FullResult {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade ={CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name="team_id")
    private Team team;

    @ManyToOne()
    //cascade={CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}
    @JoinColumn(name="tournament_id")
    private Tournament tournament;

    @Column(name="mask_results")
    private String mask_results;

    @Column(name="total_score")
    private Integer totalScore;

    @OneToMany(mappedBy = "fullResult", cascade = CascadeType.ALL
            ,orphanRemoval=true)
    @ToString.Exclude
    @Builder.Default
    private List<Controversial> controversials = new ArrayList<>();


    public void addControversialToFullResult(Controversial controversial){
        if(!controversials.contains(controversial))
        {
            controversials.add(controversial);
            controversial.setFullResult(this);
        }

    }

    public void deleteControversialFromFullResult(Controversial controversial)
    {
        if (controversials != null) {
            controversials.remove(controversial);
            controversial.setFullResult(null);
        }
    }

    public void updateTotalScoreFromMaskResults() {
        if (mask_results != null && !mask_results.isEmpty()) {
            totalScore = (int) mask_results.chars()
                    .filter(c -> c == '1')
                    .count();
        } else {
            totalScore = 0;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullResult that = (FullResult) o;
        return Objects.equals(team, that.team) && Objects.equals(tournament, that.tournament) && Objects.equals(mask_results, that.mask_results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, tournament, mask_results);
    }

    public void setMask_results(String mask_results) {
        updateTotalScoreFromMaskResults();
        this.mask_results = mask_results;
    }
}
