package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="transfers")
@JsonIdentityInfo(scope= Transfer.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Transfer {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date transferDate;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "player_id")
    //@JsonBackReference(value="player")
    private Player player;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "old_team_id")
    //@JsonBackReference(value="oldTeam")
    private Team oldTeam;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "new_team_id")
    //@JsonBackReference(value="newTeam")
    private Team newTeam;

    @Column(name = "comments")
    private String comments;

    public Transfer() {
    }

    public Transfer(int id, Date transfer_date, Team oldTeam, Player player, Team newTeam, String comments) {
        this.id = id;
        this.transferDate = transfer_date;
        this.oldTeam = oldTeam;
        this.player = player;
        this.newTeam = newTeam;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getOldTeam() {
        return oldTeam;
    }

    public void setOldTeam(Team oldTeam) {
        this.oldTeam = oldTeam;
    }

    public Team getNewTeam() {
        return newTeam;
    }

    public void setNewTeam(Team newTeam) {
        this.newTeam = newTeam;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", transfer_date=" + transferDate +
                ", player=" + player +
                ", oldTeam=" + oldTeam +
                ", newTeam=" + newTeam +
                ", comments='" + comments + '\'' +
                '}';
    }
}
