package com.studleague.studleague.dto;

import java.util.ArrayList;
import java.util.List;

public class ResultTableRow {
    private int number;
    private String teamName;
    private List<Integer> answers = new ArrayList<>();
    private int totalScore = 0;
    private List<Integer> questionNumbers = new ArrayList<>();
    private int countQuestions;

    public ResultTableRow() {
    }

    public ResultTableRow(int number, String teamName, List<Integer> answers, int totalScore, List<Integer> questionNumbers,int countQuestions) {
        this.number = number;
        this.teamName = teamName;
        this.answers = answers;
        this.totalScore = totalScore;
        this.questionNumbers = questionNumbers;
        this.countQuestions = countQuestions;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public List<Integer> getQuestionNumbers() {
        return questionNumbers;
    }

    public void setQuestionNumbers(List<Integer> questionNumbers) {
        this.questionNumbers = questionNumbers;
    }

    public int getCountQuestions() {
        return countQuestions;
    }

    public void setCountQuestions(int countQuestions) {
        this.countQuestions = countQuestions;
    }
}
