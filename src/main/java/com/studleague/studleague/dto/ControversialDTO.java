package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;

/**
 * DTO for {@link com.studleague.studleague.entities.Controversial}
 */
public class ControversialDTO {
    private int id;
    @JsonProperty("questionNumber")
    private int questionNumber;
    private String answer;
    @JsonProperty("issuedAt")
    private Date issuedAt;
    private String status;
    private String comment;
    @JsonProperty("resolvedAt")
    private String resolvedAt;
    @JsonProperty("appealJuryComment")
    private String appealJuryComment;
    private int fullResultId;

    public ControversialDTO()
    {

    }
    public ControversialDTO(int id, int questionNumber, String answer, Date issuedAt, String status, String comment, String resolvedAt, String appealJuryComment, int fullResultId) {
        this.id = id;
        this.questionNumber = questionNumber;
        this.answer = answer;
        this.issuedAt = issuedAt;
        this.status = status;
        this.comment = comment;
        this.resolvedAt = resolvedAt;
        this.appealJuryComment = appealJuryComment;
        this.fullResultId = fullResultId;
    }

    public int getId() {
        return id;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public String getResolvedAt() {
        return resolvedAt;
    }

    public String getAppealJuryComment() {
        return appealJuryComment;
    }

    public int getFullResultId() {
        return fullResultId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ControversialDTO entity = (ControversialDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.questionNumber, entity.questionNumber) &&
                Objects.equals(this.answer, entity.answer) &&
                Objects.equals(this.issuedAt, entity.issuedAt) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.comment, entity.comment) &&
                Objects.equals(this.resolvedAt, entity.resolvedAt) &&
                Objects.equals(this.appealJuryComment, entity.appealJuryComment) &&
                Objects.equals(this.fullResultId, entity.fullResultId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionNumber, answer, issuedAt, status, comment, resolvedAt, appealJuryComment, fullResultId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "questionNumber = " + questionNumber + ", " +
                "answer = " + answer + ", " +
                "issuedAt = " + issuedAt + ", " +
                "status = " + status + ", " +
                "comment = " + comment + ", " +
                "resolvedAt = " + resolvedAt + ", " +
                "appealJuryComment = " + appealJuryComment + ", " +
                "fullResultId = " + fullResultId + ")";
    }
}