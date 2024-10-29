package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByIdSite(Long idSite);

    boolean existsByIdSite(Long idSite);

    /*@Query("SELECT p FROM Player p " +
                         "JOIN Tournament t WITH p MEMBER OF t.players " +
                         "JOIN Team te WITH p MEMBER OF te.players " +
                         "WHERE t.id = :tournamentId AND te.id = :teamId")*/
    @Query("SELECT p FROM Player p " +
            "JOIN Tournament t WITH p MEMBER OF t.players " +
            "JOIN Team te WITH p MEMBER OF te.players " +
            "WHERE t.id = :tournamentId " +
            "AND te.id = :teamId " +
            "AND NOT EXISTS (" +
            "  SELECT 1 FROM Team te2 " +
            "  JOIN te2.players p2 " +
            "  WHERE p2.id = p.id AND te2.id <> :teamId AND p MEMBER OF te2.players AND t.id = :tournamentId" +
            ")")
    List<Player> findAllByTeamIdAndTournamentId(@Param("teamId") Long teamId, @Param("tournamentId") Long tournamentId );

}
