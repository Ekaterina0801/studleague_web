package com.studleague.studleague.entities;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.Objects;

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
    private LocalDate issuedAt;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Controversial that = (Controversial) o;
        return id == that.id && questionNumber == that.questionNumber && Objects.equals(answer, that.answer) && Objects.equals(issuedAt, that.issuedAt) && Objects.equals(status, that.status) && Objects.equals(comment, that.comment) && Objects.equals(resolvedAt, that.resolvedAt) && Objects.equals(appealJuryComment, that.appealJuryComment) && Objects.equals(fullResult, that.fullResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionNumber, answer, issuedAt, status, comment, resolvedAt, appealJuryComment, fullResult);
    }
}
