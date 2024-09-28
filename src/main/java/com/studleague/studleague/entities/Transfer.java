package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
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
    private Player player;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "old_team_id")
    private Team oldTeam;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "new_team_id")
    private Team newTeam;

    @Column(name = "comments")
    private String comments;

    public Transfer(int id, Date transfer_date, Team oldTeam, Player player, Team newTeam, String comments) {
        this.id = id;
        this.transferDate = transfer_date;
        this.oldTeam = oldTeam;
        this.player = player;
        this.newTeam = newTeam;
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
