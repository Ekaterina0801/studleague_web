package com.studleague.studleague.controllers;


import com.studleague.studleague.dto.team.TeamDTO;
import com.studleague.studleague.dto.team.TeamDetailsDTO;
import com.studleague.studleague.mappers.team.TeamMapper;
import com.studleague.studleague.mappers.tournament.TournamentMapper;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.implementations.SiteService;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.services.interfaces.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/site-tournaments")
@RequiredArgsConstructor
public class RatingSiteController {

    @Autowired
    private final TournamentMapper tournamentMapper;
    @Autowired
    private final TeamMapper teamMapper;
    @Autowired
    private final TournamentService tournamentService;
    @Autowired
    private final TeamService teamService;
    @Autowired
    private final SiteService siteService;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;



    /* -------------------------------------------
                      Teams
    ------------------------------------------- */

    /**
     * Обрабатывает POST запрос для добавления команды.
     *
     * @param teamId
     * @return ResponseEntity<TeamDTO>, созданный TeamDTO
     */
    @Operation(
            summary = "Создать новую команду",
            description = "Использовать для создания новой команды в системе"
    )
    @PostMapping("/leagues/{leagueId}/teams/{teamId}")
    public TeamDTO addTeam(@PathVariable long leagueId, @PathVariable long teamId) {
        return siteService.addTeamToLeagueFromSite(leagueId, teamId);
    }


    /* -------------------------------------------
                      Teams by Tournament
    ------------------------------------------- */

    /**
     * Обрабатывает POST запрос для добавления команд в турнир.
     *
     * @param leagueId идентификатор лиги
     * @param tournamentId идентификатор турнира
     * @return ResponseEntity<List<TeamDetailsDTO>>, список команд турнира
     */
    @Operation(
            summary = "Добавить команды в турнир",
            description = "Использовать для добавления команд в конкретный турнир"
    )
    @PostMapping("leagues/{leagueId}/tournaments/{tournamentId}/teams")
    public List<TeamDetailsDTO> addTournamentTeams(@PathVariable long leagueId, @PathVariable long tournamentId) {
        return siteService.addTeams(tournamentId, leagueId);
    }
}