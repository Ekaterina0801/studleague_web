package com.studleague.studleague.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "system_results")
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(scope=SystemResult.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SystemResult {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Min(value = 0, message = "Value must not be negative")
    private Integer countNotIncludedGames;

    @NotBlank
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "systemResult")
    @ToString.Exclude
    private List<League> leagues = new ArrayList<>();


}
