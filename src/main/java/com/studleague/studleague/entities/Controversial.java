package com.studleague.studleague.entities;

import jakarta.persistence.*;


import java.util.Date;

@Entity
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
    public Controversial() {
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(String resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getAppealJuryComment() {
        return appealJuryComment;
    }

    public void setAppealJuryComment(String appealJuryComment) {
        this.appealJuryComment = appealJuryComment;
    }

    public FullResult getFullResult() {
        return fullResult;
    }

    public void setFullResult(FullResult fullResult) {
        this.fullResult = fullResult;
    }
}
