package com.studleague.studleague.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.LocalDateDeserializer;
import com.studleague.studleague.entities.security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="question_number")
    @Positive
    private int questionNumber;

    @Column(name="answer")
    @NotBlank
    private String answer;

    @Column(name="issued_at")
    @ColumnDefault("'2000-01-01 10:23:54'::timestamp without time zone")
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

    @ManyToOne()
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
