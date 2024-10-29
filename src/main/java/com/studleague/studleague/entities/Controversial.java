package com.studleague.studleague.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.LocalDateDeserializer;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Long id;

    @Column(name="question_number")
    private int questionNumber;

    @Column(name="answer")
    private String answer;

    @Column(name="issued_at")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime issuedAt;

    @Column(name="status")
    private String status;

    @Column(name="comment")
    private String comment;

    @Column(name="resolved_at")
    private String resolvedAt;

    @Column(name="appeal_jury_comment")
    private String appealJuryComment;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name="result_id")
    private FullResult fullResult;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Controversial that = (Controversial) o;
        return Objects.equals(id, that.id) && questionNumber == that.questionNumber && Objects.equals(answer, that.answer) && Objects.equals(issuedAt, that.issuedAt) && Objects.equals(status, that.status) && Objects.equals(comment, that.comment) && Objects.equals(resolvedAt, that.resolvedAt) && Objects.equals(appealJuryComment, that.appealJuryComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionNumber, answer, issuedAt, status, comment, resolvedAt, appealJuryComment);
    }
}
