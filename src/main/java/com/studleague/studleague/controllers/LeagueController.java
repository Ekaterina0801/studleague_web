package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.*;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.factory.*;
import com.studleague.studleague.services.LeagueResult;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.ResultService;
import com.studleague.studleague.services.interfaces.SystemResultService;
import com.studleague.studleague.services.interfaces.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    @Autowired
    public LeagueService leagueService;

    @Autowired
    public TeamService teamService;

    @Autowired
    public TeamFactory teamFactory;

    @Autowired
    public TournamentFactory tournamentFactory;

    @Autowired
    private LeagueFactory leagueFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private LeagueResultsFactory leagueResultsFactory;

    @Autowired
    private SystemResultService systemResultService;

    @Autowired
    private SystemResultFactory systemResultFactory;

    @Autowired
    private LeagueMainInfoFactory leagueMainInfoFactory;


    /**
     * Обрабатывает GET запрос на получение всех лиг.
     *
     * @return ResponseEntity<List < LeagueDTO>>, содержащий все LeagueDTO
     */
    @Operation(
            summary = "Получить все лиги",
            description = "Использовать для получения всех лиг"
    )
    @GetMapping
    public ResponseEntity<List<LeagueDTO>> getLeagues() {
        return ResponseEntity.ok(leagueService.getAllLeagues().stream().map(x -> leagueFactory.mapToDto(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение лиги по ID.
     *
     * @param id идентификатор лиги
     * @return ResponseEntity<LeagueDTO>, содержащий данные запрашиваемого LeagueDTO
     */
    @Operation(
            summary = "Получить лигу по айди",
            description = "Использовать для получения лиги по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<LeagueMainInfoDTO> getLeague(@PathVariable long id) {
        return ResponseEntity.ok(leagueMainInfoFactory.mapToDto(leagueService.getLeagueById(id)));
    }

    @GetMapping("/system-results")
    public ResponseEntity<List<SystemResultDTO>> getSystems() {
        return ResponseEntity.ok(systemResultService.findAll().stream().map(x -> systemResultFactory.mapToDto(x)).toList());
    }

    /**
     * Обрабатывает PUT запрос на изменение системы результатов по ID лиги и ID системы результатов.
     *
     * @param leagueId       идентификатор лиги
     * @param systemResultId bдентификатор системы результатов
     * @return ResponseEntity<LeagueDTO>, содержащий данные запрашиваемого LeagueDTO
     */
    @Operation(
            summary = "Получить лигу по айди",
            description = "Использовать для получения лиги по id"
    )
    //@PreAuthorize("hasRole('ROLE_ADMIN') or @leagueService.isManager(authentication.principal.id, #leagueId)")
    @PutMapping("/{leagueId}/system-results")
    public ResponseEntity<LeagueDTO> changeSystemResult(@PathVariable long leagueId, @RequestParam long systemResultId) {
        League league = leagueService.changeSystemResultOfLeague(leagueId, systemResultId);
        return ResponseEntity.ok(leagueFactory.mapToDto(league));
    }

    /**
     * Обрабатывает POST запрос для создания новой лиги.
     *
     * @param leagueDto LeagueDTO, который нужно создать
     * @return ResponseEntity<LeagueDTO>, созданный LeagueDTO
     */
    @Operation(
            summary = "Создать новую лигу",
            description = "Использовать для создания новой лиги"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @leagueService.isManager(authentication.principal.id, #leagueDto)")
    @PostMapping
    public ResponseEntity<LeagueDTO> addNewLeague(@RequestBody LeagueDTO leagueDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        leagueDto.setCreatedById(user.getId());
        leagueDto.setManagersIds(new ArrayList<>(Collections.singletonList(user.getId())));
        leagueService.saveLeague(leagueFactory.mapToEntity(leagueDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueDto);
    }

    /**
     * Обрабатывает DELETE запрос для удаления лиги по ID.
     *
     * @param id идентификатор лиги, которую нужно удалить
     * @return ResponseEntity<String>, сообщение об успешном удалении
     */
    @Operation(
            summary = "Удалить лигу",
            description = "Использовать для удаления лиги по id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @leagueService.isManager(authentication.principal.id, #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeague(@PathVariable long id) {
        leagueService.deleteLeague(id);
        return ResponseEntity.ok("League with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает PUT запрос для добавления турнира в лигу.
     *
     * @param leagueId     идентификатор лиги
     * @param tournamentId идентификатор турнира
     * @return ResponseEntity<LeagueDTO>, обновленная LeagueDTO
     */
    @Operation(
            summary = "Добавить турнир в лигу",
            description = "Использовать для добавления турнира в лигу"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @leagueService.isManager(authentication.principal.id, #leagueId)")
    @PutMapping("/{leagueId}/tournaments/{tournamentId}")
    public ResponseEntity<LeagueDTO> addTournamentToLeague(@PathVariable long leagueId, @PathVariable long tournamentId) {
        League updatedLeague = leagueService.addTournamentToLeague(leagueId, tournamentId);
        return ResponseEntity.ok(leagueFactory.mapToDto(updatedLeague));
    }

    /**
     * Обрабатывает DELETE запрос для удаления турнира из лиги.
     *
     * @param leagueId     идентификатор лиги
     * @param tournamentId идентификатор турнира
     * @return ResponseEntity<LeagueDTO>, обновленная LeagueDTO
     */
    @Operation(
            summary = "Удалить турнир из лиги",
            description = "Использовать для удаления турнира из лиги"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @flagService.isManager(authentication.principal.id, #leagueId)")
    @DeleteMapping("/{leagueId}/tournaments/{tournamentId}")
    public ResponseEntity<LeagueDTO> deleteTournamentFromLeague(@PathVariable long leagueId, @PathVariable long tournamentId) {
        League updatedLeague = leagueService.deleteTournamentToLeague(leagueId, tournamentId);
        return ResponseEntity.ok(leagueFactory.mapToDto(updatedLeague));
    }

    /**
     * Обрабатывает GET запрос на получение всех турниров из лиги.
     *
     * @param leagueId идентификатор лиги
     * @return ResponseEntity<List < TournamentDTO>>, содержащий все TournamentDTO из лиги
     */
    @Operation(
            summary = "Все турниры из лиги",
            description = "Использовать для получения всех турниров из лиги"
    )
    @GetMapping("/{leagueId}/tournaments")
    public ResponseEntity<List<TournamentDTO>> allTournamentsFromLeague(@PathVariable long leagueId) {
        League league = leagueService.getLeagueById(leagueId);
        return ResponseEntity.ok(league.getTournaments().stream().map(x -> tournamentFactory.mapToDto(x)).toList());
    }

    /**
     * Обрабатывает GET запрос для получения команды игрока из лиги.
     *
     * @param leagueId идентификатор лиги
     * @param playerId идентификатор игрока
     * @return ResponseEntity<TeamDTO>, содержащий команду игрока
     */
    @Operation(
            summary = "Команда игрока из лиги",
            description = "Использовать для получения команды игрока из лиги"
    )
    @GetMapping("/{leagueId}/players/{playerId}/team")
    public ResponseEntity<List<TeamDTO>> teamFromLeague(@PathVariable long leagueId, @PathVariable long playerId) {
        return ResponseEntity.ok(teamService.getTeamsByPlayerIdAndLeagueId(leagueId, playerId).stream().map(x -> teamFactory.mapToDto(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение всех команд из лиги.
     *
     * @param leagueId идентификатор лиги
     * @return ResponseEntity<List < TeamDTO>>, содержащий все TeamDTO из лиги
     */
    @Operation(
            summary = "Все команды из лиги",
            description = "Использовать для получения всех команд из лиги"
    )
    @GetMapping("/{leagueId}/teams")
    public ResponseEntity<List<TeamDTO>> allTeamsFromLeague(@PathVariable long leagueId) {
        return ResponseEntity.ok(teamService.teamsByLeague(leagueId).stream().map(x -> teamFactory.mapToDto(x)).toList());
    }

    /**
     * Обрабатывает DELETE запрос для удаления всех лиг.
     *
     * @return ResponseEntity<String>, сообщение об успешном удалении всех лиг
     */
    @Operation(
            summary = "Удалить все лиги",
            description = "Использовать для удаления всех лиг"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<String> deleteAllLeagues() {
        leagueService.deleteAllLeagues();
        return ResponseEntity.ok("Leagues were deleted");
    }

    @GetMapping("/{leagueId}/results")
    public ResponseEntity<List<LeagueResultsDTO>> getLeagueResults(@PathVariable long leagueId) {
        List<LeagueResult> results = resultService.calculateResultsBySystem(leagueId);
        return ResponseEntity.ok(results.stream().map(x -> leagueResultsFactory.mapToDto(x)).toList());
    }

}
