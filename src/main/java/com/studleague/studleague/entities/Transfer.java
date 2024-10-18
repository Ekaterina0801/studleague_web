package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Table(name="transfers")
@JsonIdentityInfo(scope= Transfer.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Transfer {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(transferDate, transfer.transferDate) && Objects.equals(player, transfer.player) && Objects.equals(oldTeam, transfer.oldTeam) && Objects.equals(newTeam, transfer.newTeam) && Objects.equals(comments, transfer.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferDate, player, oldTeam, newTeam, comments);
    }
}
