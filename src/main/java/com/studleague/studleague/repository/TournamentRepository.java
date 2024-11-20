package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>, JpaSpecificationExecutor<Tournament> {

    Optional<Tournament> findByIdSite(Long idSite);

    @Query("SELECT t FROM Tournament t JOIN t.teams te WHERE te.id = :teamId")
    List<Tournament> findAllByTeamId(@Param("teamId") Long teamId);

    boolean existsByIdSite(Long idSite);

}
