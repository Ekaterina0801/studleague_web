package com.studleague.studleague.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Table(name="controversials")
public class Controversial {

    @Id
    @Column(name="id")
    private long id;

    @Column(name="questionNumber")
    private int questionNumber;

    @Column(name="answer")
    private String answer;

    @Column(name="issuedAt")
    private Date issuedAt;

    @Column(name="status")
    private String status;

    @Column(name="comment")
    private String comment;

    @Column(name="resolvedAt")
    private String resolvedAt;

    @Column(name="appealJuryComment")
    private String appealJuryComment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="result_id")
    private FullResult fullResult;

}
