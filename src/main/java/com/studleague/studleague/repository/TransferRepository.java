package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findAllByPlayerId(long playerId);

    @Query("select t from Transfer t left join fetch t.oldTeam o left join fetch t.newTeam n where o.id=:teamId or n.id=:teamId")
    List<Transfer> findAllByTeamId(@Param("teamId")long teamId);

}
