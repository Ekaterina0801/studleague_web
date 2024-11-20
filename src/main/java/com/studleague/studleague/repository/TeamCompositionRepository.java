package com.studleague.studleague.repository;

import com.studleague.studleague.entities.TeamComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamCompositionRepository extends JpaRepository<TeamComposition, Long>, JpaSpecificationExecutor<TeamComposition> {

    List<TeamComposition> findAllByTournamentId(Long tournamentId);

    List<TeamComposition> findAllByParentTeamId(Long parentTeamId);

    Optional<TeamComposition> findByTournamentIdAndParentTeamId(Long tournamentId, Long parentTeamId);

}
