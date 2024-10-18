package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Table(name="fullResults")
@JsonIdentityInfo(scope = FullResult.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FullResult {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name="team_id",nullable = false)
    private Team team;

    @ManyToOne()
    @JoinColumn(name="tournament_id",nullable = false)
    @JsonBackReference
    private Tournament tournament;

    @Column(name="mask_results")
    private String mask_results;

    @OneToMany(mappedBy = "fullResult", cascade = CascadeType.ALL,orphanRemoval=true)
    @ToString.Exclude
    private List<Controversial> controversials = new ArrayList<>();


    public void addControversialToFullResult(Controversial controversial){
        if (controversials==null)
        {
            controversials = new ArrayList<>();
        }
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

}
