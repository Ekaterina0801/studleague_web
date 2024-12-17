package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.tournament.TournamentDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public interface TournamentService {
    Tournament getTournamentById(Long id);

    List<Tournament> getAllTournaments();

    void saveTournament(Tournament tournament);

    void deleteTournament(Long id);

    Tournament addResultToTournament(Long tournamentId, Long resultId);

    Tournament deleteResultFromTournament(Long tournamentId, Long resultId);

    Tournament addPlayerToTournament(Long tournamentId, Long playerId);

    Tournament deletePlayerFromTournament(Long tournamentId, Long playerId);

    Tournament deleteTeamFromTournament(Long tournamentId, Long teamId);

    Tournament addTeamToTournament(Long tournamentId, Long teamId);

    Tournament addTeamAndPlayerToTournament(Long tournamentId, Long teamId, Long playerId);

    Tournament getTournamentBySiteId(Long idSite);

    boolean existsByIdSite(Long idSite);

    void deleteAllTournaments();

    HashMap<Team, List<Player>> getTeamsPlayersByTournamentId(Long tournamentId);

    boolean isManager(Long userId, Long tournamentId);

    boolean isManager(Long userId, TournamentDTO tournamentDTO);

    Page<Tournament> searchTournaments(String name, Long leagueId, LocalDateTime startDate, LocalDateTime endDate, Sort sort, Pageable pageable);
}
