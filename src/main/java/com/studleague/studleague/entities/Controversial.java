package com.studleague.studleague.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="controversials")
public class Controversial {

    @Id
    @Column(name="id")
    private int id;

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

    public Controversial(int id, int questionNumber, String answer, Date issuedAt, String comment, String status, String resolvedAt, String appealJuryComment) {
        this.id = id;
        this.questionNumber = questionNumber;
        this.answer = answer;
        this.issuedAt = issuedAt;
        this.comment = comment;
        this.status = status;
        this.resolvedAt = resolvedAt;
        this.appealJuryComment = appealJuryComment;
    }
}
