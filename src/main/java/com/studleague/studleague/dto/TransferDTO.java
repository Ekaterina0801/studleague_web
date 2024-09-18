package com.studleague.studleague.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class TransferDTO {
    private int id;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date transferDate;
    @JsonProperty("playerId")
    private int playerId;
    @JsonProperty("oldTeamId")
    private int oldTeamId;
    @JsonProperty("newTeamId")
    private int newTeamId;
    private String comments;

    public TransferDTO() {}

    public TransferDTO(int id, Date transferDate, int playerId, int oldTeamId, int newTeamId, String comments) {
        this.id = id;
        this.transferDate = transferDate;
        this.playerId = playerId;
        this.oldTeamId = oldTeamId;
        this.newTeamId = newTeamId;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getOldTeamId() {
        return oldTeamId;
    }

    public void setOldTeamId(int oldTeamId) {
        this.oldTeamId = oldTeamId;
    }

    public int getNewTeamId() {
        return newTeamId;
    }

    public void setNewTeamId(int newTeamId) {
        this.newTeamId = newTeamId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
