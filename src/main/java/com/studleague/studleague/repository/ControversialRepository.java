package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Controversial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ControversialRepository extends JpaRepository<Controversial, Long> {

    @Query("SELECT c FROM Controversial c WHERE c.fullResult.team.id = :teamId")
    List<Controversial> findAllByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT c FROM Controversial c WHERE c.fullResult.tournament.id = :tournamentId")
    List<Controversial> findAllByTournamentId(@Param("tournamentId") Long tournamentId);

    boolean existsByFullResultIdAndQuestionNumber(Long fullResultId, int questionNumber);

    Optional<Controversial> findByFullResultIdAndQuestionNumber(Long fullResultId, int questionNumber);


}
