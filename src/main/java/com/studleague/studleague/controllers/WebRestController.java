package com.studleague.studleague.controllers;
import com.studleague.studleague.dto.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.factory.*;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.List;
@RestController
@RequestMapping("/api")
public class WebRestController {

    @Autowired
    public FlagService flagService;

    @Autowired
    public ResultService resultService;

    @Autowired
    public LeagueService leagueService;

    @Autowired
    public PlayerService playerService;

    @Autowired
    public TeamService teamService;

    @Autowired
    public TournamentService tournamentService;

    @Autowired
    public TransferService transferService;

    @Autowired
    public TransferFactory transferFactory;

    @Autowired
    public TeamFactory teamFactory;

    @Autowired
    public FullResultFactory fullResultFactory;

    @Autowired
    public ControversialFactory controversialFactory;

    @Autowired
    public TournamentFactory tournamentFactory;

    @Autowired
    public ControversialService controversialService;

    @Autowired
    public PlayerFactory playerFactory;

    @Autowired
    private FlagFactory flagFactory;

    @Autowired
    private LeagueFactory leagueFactory;

    @Autowired
    private UserService userService;

    /* -------------------------------------------
                      Controversials
    ------------------------------------------- */

    /**
     * Обрабатывает GET запрос на получение всех Controversial.
     *
     * @return ResponseEntity<List<ControversialDTO>>, содержащий все ControversialDTO
     */
    @Operation(
            summary = "Получить все спорные",
            description = "Использовать для получения всех спорных"
    )
    @GetMapping("/controversials")
    public ResponseEntity<List<ControversialDTO>> getControversials() {
        return ResponseEntity.ok(controversialService.getAllControversials().stream().map(x -> controversialFactory.toDto(x)).toList());
    }

    /**
     * Обрабатывает POST запрос для создания нового Controversial.
     *
     * @param controversialDto ControversialDTO, который нужно создать
     * @return ResponseEntity<ControversialDTO>, созданный ControversialDTO
     */
    @Operation(
            summary = "Создать новый спорный",
            description = "Использовать для создания нового спорного"
    )
    @PostMapping("/controversials")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @controversialService.isManager(authentication.principal.id, #controversialDto)")
    public ResponseEntity<ControversialDTO> addNewControversial(@RequestBody ControversialDTO controversialDto) {
        Controversial controversial = controversialFactory.toEntity(controversialDto);
        controversialService.saveControversial(controversial);
        return ResponseEntity.status(HttpStatus.CREATED).body(controversialDto);
    }

    /**
     * Обрабатывает GET запрос для получения Controversial по ID.
     *
     * @param id идентификатор Controversial
     * @return ResponseEntity<ControversialDTO>, содержащий данные запрашиваемого ControversialDTO
     */
    @Operation(
            summary = "Получить спорный по айди",
            description = "Использовать для получения спорного по id"
    )
    @GetMapping("/controversials/{id}")
    public ResponseEntity<ControversialDTO> controversialById(@PathVariable long id) {
        Controversial controversial = controversialService.getControversialById(id);
        ControversialDTO controversialDTO = controversialFactory.toDto(controversial);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(controversialDTO);
    }

    /**
     * Обрабатывает DELETE запрос для удаления Controversial по ID.
     *
     * @param id идентификатор Controversial, который нужно удалить
     * @return ResponseEntity<String>, сообщение об успешном удалении
     */
    @Operation(
            summary = "Удалить спорный",
            description = "Использовать для удаления спорного по id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @controversialService.isManager(authentication.principial.id, #id)")
    @DeleteMapping("/controversials/{id}")
    public ResponseEntity<String> deleteControversial(@PathVariable long id) {
        controversialService.deleteControversial(id);
        return ResponseEntity.ok("Controversial with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает DELETE запрос для удаления всех Controversials.
     *
     * @return ResponseEntity<String>, сообщение об успешном удалении всех Controversials
     */
    @Operation(
            summary = "Удалить все спорные",
            description = "Использовать для удаления всех спорных"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/controversials")
    public ResponseEntity<String> deleteAllControversials() {
        controversialService.deleteAllControversials();
        return ResponseEntity.ok("Controversials were deleted");
    }

    /* -------------------------------------------
                      Flags
    ------------------------------------------- */

    /**
     * Обрабатывает GET запрос на получение всех флагов.
     *
     * @return ResponseEntity<List<FlagDTO>>, содержащий все FlagDTO
     */
    @Operation(
            summary = "Получить все флаги",
            description = "Использовать для получения всех флагов"
    )
    @GetMapping("/flags")
    public ResponseEntity<List<FlagDTO>> getFlags() {
        return ResponseEntity.ok(flagService.getAllFlags().stream().map(x -> flagFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение флага по ID.
     *
     * @param id идентификатор флага
     * @return ResponseEntity<FlagDTO>, содержащий данные запрашиваемого FlagDTO
     */
    @Operation(
            summary = "Получить флаг по айди",
            description = "Использовать для получения флага по id"
    )
    @GetMapping("/flags/{id}")
    public ResponseEntity<FlagDTO> getFlag(@PathVariable long id) {
        return ResponseEntity.ok(flagFactory.toDTO(flagService.getFlagById(id)));
    }

    /**
     * Обрабатывает DELETE запрос для удаления флага по ID.
     *
     * @param id идентификатор флага, который нужно удалить
     * @return ResponseEntity<String>, сообщение об успешном удалении
     */
    @Operation(
            summary = "Удалить флаг",
            description = "Использовать для удаления флага по id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @flagService.isManager(authentication.principal.id, #id)")
    @DeleteMapping("/flags/{id}")
    public ResponseEntity<String> deleteFlag(@PathVariable long id) {
        flagService.deleteFlag(id);
        return ResponseEntity.ok("Flag with ID = " + id + " was deleted");
    }


    /**
     * Обрабатывает POST запрос для создания нового флага.
     *
     * @param flagDto FlagDTO, который нужно создать
     * @return ResponseEntity<FlagDTO>, созданный FlagDTO
     */
    @Operation(
            summary = "Создать новый флаг",
            description = "Использовать для создания нового флага"
    )
    @PostMapping("/flags")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @flagService.isManager(authorization.principal.id, #flagDto)")
    public ResponseEntity<FlagDTO> addNewFlag(@RequestBody FlagDTO flagDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            authorities.forEach(authority -> {
                System.out.println("Granted Authority: " + authority.getAuthority());
            });
        }

        flagService.saveFlag(flagFactory.toEntity(flagDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(flagDto);
    }

    /**
     * Обрабатывает DELETE запрос для удаления всех флагов.
     *
     * @return ResponseEntity<String>, сообщение об успешном удалении всех флагов
     */
    @Operation(
            summary = "Удалить все флаги",
            description = "Использовать для удаления всех флагов"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/flags")
    public ResponseEntity<String> deleteAllFlags() {
        flagService.deleteAllFlags();
        return ResponseEntity.ok("Flags were deleted");
    }

    /**
     * Обрабатывает GET запрос на получение команд по Flag.
     *
     * @param id идентификатор флага
     * @return ResponseEntity<List<TeamDTO>>, содержащий данные TeamDTO по флагу
     */
    @Operation(
            summary = "Получить флаг по айди",
            description = "Использовать для получения флага по id"
    )
    @GetMapping("/flags/{id}/teams")
    public ResponseEntity<List<TeamDTO>> getTeamsByFlagId(@PathVariable long id) {
        return ResponseEntity.ok(teamService.getTeamsByFlagId(id).stream().map(x-> teamFactory.toDTO(x)).toList());
    }

    /* -------------------------------------------
                      Leagues
    ------------------------------------------- */

    /**
     * Обрабатывает GET запрос на получение всех лиг.
     *
     * @return ResponseEntity<List<LeagueDTO>>, содержащий все LeagueDTO
     */
    @Operation(
            summary = "Получить все лиги",
            description = "Использовать для получения всех лиг"
    )
    @GetMapping("/leagues")
    public ResponseEntity<List<LeagueDTO>> getLeagues() {
        return ResponseEntity.ok(leagueService.getAllLeagues().stream().map(x -> leagueFactory.toDTO(x)).toList());
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
    @GetMapping("/leagues/{id}")
    public ResponseEntity<LeagueDTO> getLeague(@PathVariable long id) {
        return ResponseEntity.ok(leagueFactory.toDTO(leagueService.getLeagueById(id)));
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
    @PostMapping("/leagues")
    public ResponseEntity<LeagueDTO> addNewLeague(@RequestBody LeagueDTO leagueDto) {
        leagueService.saveLeague(leagueFactory.toEntity(leagueDto));
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
    @DeleteMapping("/leagues/{id}")
    public ResponseEntity<String> deleteLeague(@PathVariable long id) {
        leagueService.deleteLeague(id);
        return ResponseEntity.ok("League with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает PUT запрос для добавления турнира в лигу.
     *
     * @param leagueId идентификатор лиги
     * @param tournamentId идентификатор турнира
     * @return ResponseEntity<LeagueDTO>, обновленная LeagueDTO
     */
    @Operation(
            summary = "Добавить турнир в лигу",
            description = "Использовать для добавления турнира в лигу"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @leagueService.isManager(authentication.principal.id, #leagueId)")
    @PutMapping("/leagues/{leagueId}/tournaments/{tournamentId}")
    public ResponseEntity<LeagueDTO> addTournamentToLeague(@PathVariable long leagueId, @PathVariable long tournamentId) {
        League updatedLeague = leagueService.addTournamentToLeague(leagueId, tournamentId);
        return ResponseEntity.ok(leagueFactory.toDTO(updatedLeague));
    }

    /**
     * Обрабатывает DELETE запрос для удаления турнира из лиги.
     *
     * @param leagueId идентификатор лиги
     * @param tournamentId идентификатор турнира
     * @return ResponseEntity<LeagueDTO>, обновленная LeagueDTO
     */
    @Operation(
            summary = "Удалить турнир из лиги",
            description = "Использовать для удаления турнира из лиги"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @flagService.isManager(authentication.principal.id, #leagueId)")
    @DeleteMapping("/leagues/{leagueId}/tournaments/{tournamentId}")
    public ResponseEntity<LeagueDTO> deleteTournamentFromLeague(@PathVariable long leagueId, @PathVariable long tournamentId) {
        League updatedLeague = leagueService.deleteTournamentToLeague(leagueId, tournamentId);
        return ResponseEntity.ok(leagueFactory.toDTO(updatedLeague));
    }

    /**
     * Обрабатывает GET запрос на получение всех турниров из лиги.
     *
     * @param leagueId идентификатор лиги
     * @return ResponseEntity<List<TournamentDTO>>, содержащий все TournamentDTO из лиги
     */
    @Operation(
            summary = "Все турниры из лиги",
            description = "Использовать для получения всех турниров из лиги"
    )
    @GetMapping("/leagues/{leagueId}/tournaments")
    public ResponseEntity<List<TournamentDTO>> allTournamentsFromLeague(@PathVariable long leagueId) {
        League league = leagueService.getLeagueById(leagueId);
        return ResponseEntity.ok(league.getTournaments().stream().map(x -> tournamentFactory.toDTO(x)).toList());
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
    @GetMapping("/leagues/{leagueId}/players/{playerId}/team")
    public ResponseEntity<List<TeamDTO>> teamFromLeague(@PathVariable long leagueId, @PathVariable long playerId) {
        return ResponseEntity.ok(teamService.getTeamsByPlayerIdAndLeagueId(leagueId, playerId).stream().map(x-> teamFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение всех команд из лиги.
     *
     * @param leagueId идентификатор лиги
     * @return ResponseEntity<List<TeamDTO>>, содержащий все TeamDTO из лиги
     */
    @Operation(
            summary = "Все команды из лиги",
            description = "Использовать для получения всех команд из лиги"
    )
    @GetMapping("/leagues/{leagueId}/teams")
    public ResponseEntity<List<TeamDTO>> allTeamsFromLeague(@PathVariable long leagueId) {
        return ResponseEntity.ok(teamService.teamsByLeague(leagueId).stream().map(x -> teamFactory.toDTO(x)).toList());
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
    @DeleteMapping("/leagues")
    public ResponseEntity<String> deleteAllLeagues() {
        leagueService.deleteAllLeagues();
        return ResponseEntity.ok("Leagues were deleted");
    }


    /* -------------------------------------------
                      Players
    ------------------------------------------- */

    /**
     * Обрабатывает GET запрос на получение всех игроков.
     *
     * @return ResponseEntity<List<PlayerDTO>>, содержащий все PlayerDTO
     */
    @Operation(
            summary = "Получить всех игроков",
            description = "Использовать для получения всех игроков"
    )
    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers().stream().map(x -> playerFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение игрока по ID.
     *
     * @param id идентификатор игрока
     * @return ResponseEntity<PlayerDTO>, содержащий данные запрашиваемого PlayerDTO
     */
    @Operation(
            summary = "Получить игрока по айди",
            description = "Использовать для получения игрока по id"
    )
    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable long id) {
        return ResponseEntity.ok(playerFactory.toDTO(playerService.getPlayerById(id)));
    }

    /**
     * Обрабатывает POST запрос для создания нового игрока.
     *
     * @param playerDTO PlayerDTO, который нужно создать
     * @return ResponseEntity<PlayerDTO>, созданный PlayerDTO
     */
    @Operation(
            summary = "Создать нового игрока",
            description = "Использовать для создания нового игрока"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @playerService.isManager(authentication.principal.id, #playerDTO)")
    @PostMapping("/players")
    public ResponseEntity<PlayerDTO> addNewPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = playerFactory.toEntity(playerDTO);
        playerService.savePlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(playerDTO);
    }

    /**
     * Обрабатывает DELETE запрос для удаления игрока по ID.
     *
     * @param id идентификатор игрока, которого нужно удалить
     * @return ResponseEntity<String>, сообщение об успешном удалении
     */
    @Operation(
            summary = "Удалить игрока",
            description = "Использовать для удаления игрока по id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @playerService.isManager(authentication.principal.id, #id)")
    @DeleteMapping("/players/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok("Player with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает GET запрос на получение всех трансферов игрока.
     *
     * @param playerId идентификатор игрока
     * @return ResponseEntity<List<TransferDTO>>, содержащий все трансферы игрока
     */
    @Operation(
            summary = "Все трансферы игрока",
            description = "Использовать для получения всех трансферов игрока"
    )
    @GetMapping("/players/{playerId}/transfers")
    public ResponseEntity<List<TransferDTO>> allTransfersFromPlayer(@PathVariable long playerId) {
        return ResponseEntity.ok(transferService.getTransfersForPlayer(playerId).stream().map(x -> transferFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение всех команд игрока.
     *
     * @param playerId идентификатор игрока
     * @return ResponseEntity<List<TeamDTO>>, содержащий все команды игрока
     */
    @Operation(
            summary = "Все команды игрока",
            description = "Использовать для получения всех команд игрока"
    )
    @GetMapping("/players/{playerId}/teams")
    public ResponseEntity<List<TeamDTO>> allTeamsFromPlayer(@PathVariable long playerId) {
        return ResponseEntity.ok(teamService.getTeamsByPlayerId(playerId).stream().map(x -> teamFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение всех турниров игрока.
     *
     * @param playerId идентификатор игрока
     * @return ResponseEntity<List<TournamentDTO>>, содержащий все турниры игрока
     */
    @Operation(
            summary = "Все турниры игрока",
            description = "Использовать для получения всех турниров игрока"
    )
    @GetMapping("/players/{playerId}/tournaments")
    public ResponseEntity<List<TournamentDTO>> allTournamentsFromPlayer(@PathVariable long playerId) {
        Player player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(player.getTournaments().stream().map(x -> tournamentFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает DELETE запрос для удаления всех игроков.
     *
     * @return ResponseEntity<String>, сообщение об успешном удалении всех игроков
     */
    @Operation(
            summary = "Удалить всех игроков",
            description = "Использовать для удаления всех игроков"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/players")
    public ResponseEntity<String> deleteAllPlayers() {
        playerService.deleteAllPlayers();
        return ResponseEntity.ok("Players were deleted");
    }

    /* -------------------------------------------
                      Results
    ------------------------------------------- */

    /**
     * Обрабатывает GET запрос на получение всех результатов.
     *
     * @return ResponseEntity<List<FullResultDTO>>, содержащий все FullResultDTO
     */
    @Operation(
            summary = "Получить все результаты",
            description = "Использовать для получения всех результатов"
    )
    @GetMapping("/results")
    public ResponseEntity<List<FullResultDTO>> getResults() {
        return ResponseEntity.ok(resultService.getAllFullResults().stream().map(x -> fullResultFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение результата по ID.
     *
     * @param id идентификатор результата
     * @return ResponseEntity<FullResultDTO>, содержащий данные запрашиваемого FullResultDTO
     */
    @Operation(
            summary = "Получить результат по айди",
            description = "Использовать для получения результата по id"
    )
    @GetMapping("/results/{id}")
    public ResponseEntity<FullResultDTO> getResult(@PathVariable long id) {
        return ResponseEntity.ok(fullResultFactory.toDTO(resultService.getFullResultById(id)));
    }

    /**
     * Обрабатывает POST запрос для создания нового результата.
     *
     * @param fullResultDTO FullResultDTO, который нужно создать
     * @return ResponseEntity<FullResultDTO>, созданный FullResultDTO
     */
    @Operation(
            summary = "Создать новый результат",
            description = "Использовать для создания нового результата"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @ResultService.isManager(authentication.principal.id, #fullResultDTO)")
    @PostMapping("/results")
    public ResponseEntity<FullResultDTO> addNewResult(@RequestBody FullResultDTO fullResultDTO) {
        if (fullResultDTO == null) {
                    return ResponseEntity.badRequest().build();
                }
        FullResult fullResult = fullResultFactory.toEntity(fullResultDTO);
        resultService.saveFullResult(fullResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(fullResultDTO);
    }

    /**
     * Обрабатывает DELETE запрос для удаления результата по ID.
     *
     * @param id идентификатор результата, который нужно удалить
     * @return ResponseEntity<String>, сообщение об успешном удалении
     */
    @Operation(
            summary = "Удалить результат",
            description = "Использовать для удаления результата по id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @resultService.isManager(authentication.principal.id, #id)")
    @DeleteMapping("/results/{id}")
    public ResponseEntity<String> deleteFullResult(@PathVariable long id) {
        resultService.deleteFullResult(id);
        return ResponseEntity.ok("FullResult with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает GET запрос на получение всех спорных результатов по ID результата.
     *
     * @param resultId идентификатор результата
     * @return ResponseEntity<List<ControversialDTO>>, содержащий все спорные результаты
     */
    @Operation(
            summary = "Все спорные для результата",
            description = "Использовать для получения всех спорных результатов"
    )
    @GetMapping("/results/{resultId}/controversials")
    public ResponseEntity<List<ControversialDTO>> allControversialsFromResult(@PathVariable long resultId) {
        FullResult fullResult = resultService.getFullResultById(resultId);
        return ResponseEntity.ok(fullResult.getControversials().stream().map(x -> controversialFactory.toDto(x)).toList());
    }

    /**
     * Обрабатывает PUT запрос для добавления спорного в результат.
     *
     * @param resultId идентификатор результата
     * @param controversialId идентификатор спорного
     * @return ResponseEntity<FullResultDTO>, обновленный FullResultDTO
     */
    @Operation(
            summary = "Добавить спорный в результат",
            description = "Использовать для добавления спорного в результат"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @resultService.isManager(authentication.principal.id, #resultId)")
    @PutMapping("/results/{resultId}/controversials/{controversialId}")
    public ResponseEntity<FullResultDTO> addControversialToResult(@PathVariable long resultId, @PathVariable long controversialId) {
        FullResult fullResult = resultService.addControversialToResult(resultId, controversialId);
        return ResponseEntity.ok(fullResultFactory.toDTO(fullResult));
    }

    /**
     * Обрабатывает DELETE запрос для удаления всех результатов.
     *
     * @return ResponseEntity<String>, сообщение об успешном удалении всех результатов
     */
    @Operation(
            summary = "Удалить все результаты",
            description = "Использовать для удаления всех результатов"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/results")
    public ResponseEntity<String> deleteAllResults() {
        resultService.deleteAllResults();
        return ResponseEntity.ok("Results were deleted");
    }

    /* -------------------------------------------
                      Teams
    ------------------------------------------- */

    /**
     * Обрабатывает GET запрос на получение всех команд.
     *
     * @return ResponseEntity<List<TeamDTO>>, содержащий все TeamDTO
     */
    @Operation(
            summary = "Получить все команды",
            description = "Использовать для получения всех команд"
    )
    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getTeams() {
        return ResponseEntity.ok(teamService.getAllTeams().stream().map(x -> teamFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение команды по ID.
     *
     * @param id идентификатор команды
     * @return ResponseEntity<TeamDTO>, содержащий данные запрашиваемого TeamDTO
     */
    @Operation(
            summary = "Получить команду по айди",
            description = "Использовать для получения команды по id"
    )
    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDTO> getTeam(@PathVariable long id) {
        return ResponseEntity.ok(teamFactory.toDTO(teamService.getTeamById(id)));
    }

    /**
     * Обрабатывает POST запрос для создания новой команды.
     *
     * @param teamDTO TeamDTO, который нужно создать
     * @return ResponseEntity<TeamDTO>, созданный TeamDTO
     */
    @Operation(
            summary = "Создать новую команду",
            description = "Использовать для создания новой команды"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @teamService.isManager(authentication.principal.id, #teamDTO)")
    @PostMapping(value = "/teams", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDTO> addNewTeam(@RequestBody TeamDTO teamDTO) {
        Team team = teamFactory.toEntity(teamDTO);
        teamService.saveTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamDTO);
    }

    /**
     * Обрабатывает DELETE запрос для удаления команды по ID.
     *
     * @param id идентификатор команды, которую нужно удалить
     * @return ResponseEntity<String>, сообщение об успешном удалении
     */
    @Operation(
            summary = "Удалить команду",
            description = "Использовать для удаления команды по id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @teamService.isManager(authentication.principal.id, #id)")
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok("Team with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает PUT запрос для добавления игрока в команду.
     *
     * @param teamId идентификатор команды
     * @param playerId идентификатор игрока
     * @return ResponseEntity<TeamDTO>, обновленная TeamDTO
     */
    @Operation(
            summary = "Добавить игрока в команду",
            description = "Использовать для добавления игрока в команду"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @teamService.isManager(authentication.principal.id, #teamId)")
    @PutMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDTO> addPlayerToTeam(@PathVariable long teamId, @PathVariable long playerId) {
        Team updatedTeam = teamService.addPlayerToTeam(teamId, playerId);
        return ResponseEntity.ok(teamFactory.toDTO(updatedTeam));
    }

    /**
     * Обрабатывает DELETE запрос для удаления игрока из команды.
     *
     * @param teamId идентификатор команды
     * @param playerId идентификатор игрока
     * @return ResponseEntity<TeamDTO>, обновленная TeamDTO
     */
    @Operation(
            summary = "Удалить игрока из команды",
            description = "Использовать для удаления игрока из команды"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @teamService.isManager(authentication.principal.id, #teamId)")
    @DeleteMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDTO> deletePlayerFromTeam(@PathVariable long teamId, @PathVariable long playerId) {
        Team updatedTeam = teamService.deletePlayerFromTeam(teamId, playerId);
        return ResponseEntity.ok(teamFactory.toDTO(updatedTeam));
    }

    /**
     * Обрабатывает PUT запрос для добавления флага в команду.
     *
     * @param teamId идентификатор команды
     * @param flagId идентификатор флага
     * @return ResponseEntity<TeamDTO>, обновленная TeamDTO
     */
    @Operation(
            summary = "Добавить флаг в команду",
            description = "Использовать для добавления флага в команду"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @teamService.isManager(authentication.principal.id, #teamId)")
    @PutMapping("/teams/{teamId}/flags/{flagId}")
    public ResponseEntity<TeamDTO> addFlagToTeam(@PathVariable long teamId, @PathVariable long flagId) {
        Team updatedTeam = teamService.addFlagToTeam(teamId, flagId);
        return ResponseEntity.ok(teamFactory.toDTO(updatedTeam));
    }

    /**
     * Обрабатывает PUT запрос для добавления лиги в команду.
     *
     * @param teamId идентификатор команды
     * @param leagueId идентификатор лиги
     * @return ResponseEntity<TeamDTO>, обновленная TeamDTO
     */
    @Operation(
            summary = "Добавить лигу в команду",
            description = "Использовать для добавления лиги в команду"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @leagueService.isManager(authentication.principal.id, #leagueId)")
    @PutMapping("/teams/{teamId}/leagues/{leagueId}")
    public ResponseEntity<TeamDTO> addLeagueToTeam(@PathVariable long teamId, @PathVariable long leagueId) {
        Team updatedTeam = teamService.addLeagueToTeam(teamId, leagueId);
        return ResponseEntity.ok(teamFactory.toDTO(updatedTeam));
    }

    /**
     * Обрабатывает DELETE запрос для удаления флага из команды.
     *
     * @param teamId идентификатор команды
     * @param flagId идентификатор флага
     * @return ResponseEntity<TeamDTO>, обновленная TeamDTO
     */
    @Operation(
            summary = "Удалить флаг из команды",
            description = "Использовать для удаления флага из команды"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @teamService.isManager(authentication.principal.id, #teamId)")
    @DeleteMapping("/teams/{teamId}/flags/{flagId}")
    public ResponseEntity<TeamDTO> deleteFlagFromTeam(@PathVariable long teamId, @PathVariable long flagId) {
        Team updatedTeam = teamService.deleteFlagFromTeam(teamId, flagId);
        return ResponseEntity.ok(teamFactory.toDTO(updatedTeam));
    }

    /**
     * Обрабатывает GET запрос на получение всех игроков из команды.
     *
     * @param teamId идентификатор команды
     * @return ResponseEntity<List<PlayerDTO>>, содержащий всех игроков из команды
     */
    @Operation(
            summary = "Все игроки из команды",
            description = "Использовать для получения всех игроков из команды"
    )
    @GetMapping("/teams/{teamId}/players")
    public ResponseEntity<List<PlayerDTO>> allPlayersFromTeam(@PathVariable long teamId) {
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team.getPlayers().stream().map(x -> playerFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение всех турниров из команды.
     *
     * @param teamId идентификатор команды
     * @return ResponseEntity<List<TournamentDTO>>, содержащий все турниры из команды
     */
    @Operation(
            summary = "Все турниры из команды",
            description = "Использовать для получения всех турниров из команды"
    )
    @GetMapping("/teams/{teamId}/tournaments")
    public ResponseEntity<List<TournamentDTO>> allTournamentsFromTeam(@PathVariable long teamId) {
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team.getTournaments().stream().map(x -> tournamentFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение всех флагов из команды.
     *
     * @param teamId идентификатор команды
     * @return ResponseEntity<List<FlagDTO>>, содержащий все флаги из команды
     */
    @Operation(
            summary = "Все флаги из команды",
            description = "Использовать для получения всех флагов из команды"
    )
    @GetMapping("/teams/{teamId}/flags")
    public ResponseEntity<List<FlagDTO>> allFlagsFromTeam(@PathVariable long teamId) {
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team.getFlags().stream().map(x -> flagFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение всех трансферов из команды.
     *
     * @param teamId идентификатор команды
     * @return ResponseEntity<List<TransferDTO>>, содержащий все трансферы из команды
     */
    @Operation(
            summary = "Все трансферы из команды",
            description = "Использовать для получения всех трансферов из команды"
    )
    @GetMapping("/teams/{teamId}/transfers")
    public ResponseEntity<List<TransferDTO>> allTransfersFromTeam(@PathVariable long teamId) {
        return ResponseEntity.ok(transferService.getTransfersForTeam(teamId).stream().map(x -> transferFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение всех результатов команды.
     *
     * @param teamId идентификатор команды
     * @return ResponseEntity<List<FullResultDTO>>, содержащий все результаты команды
     */
    @Operation(
            summary = "Все результаты команды",
            description = "Использовать для получения всех результатов команды"
    )
    @GetMapping("/teams/{teamId}/results")
    public ResponseEntity<List<FullResultDTO>> allResultsForTeam(@PathVariable long teamId) {
        return ResponseEntity.ok(resultService.getResultsForTeam(teamId).stream().map(x -> fullResultFactory.toDTO(x)).toList());
    }

    /* -------------------------------------------
                      Tournaments
    ------------------------------------------- */

    /**
     * Обрабатывает GET запрос на получение всех турниров.
     *
     * @return ResponseEntity<List<TournamentDTO>>, содержащий все TournamentDTO
     */
    @Operation(
            summary = "Получить все турниры",
            description = "Использовать для получения всех турниров"
    )
    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentDTO>> getTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments().stream().map(x -> tournamentFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение турнира по ID.
     *
     * @param id идентификатор турнира
     * @return ResponseEntity<TournamentDTO>, содержащий данные запрашиваемого TournamentDTO
     */
    @Operation(
            summary = "Получить турнир по айди",
            description = "Использовать для получения турнира по id"
    )
    @GetMapping("/tournaments/{id}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable long id) {
        return ResponseEntity.ok(tournamentFactory.toDTO(tournamentService.getTournamentById(id)));
    }

    /**
     * Обрабатывает POST запрос для создания нового турнира.
     *
     * @param tournamentDto TournamentDTO, который нужно создать
     * @return ResponseEntity<TournamentDTO>, созданный TournamentDTO
     */
    @Operation(
            summary = "Создать новый турнир",
            description = "Использовать для создания нового турнира"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tournamentService.isManager(authentication.principal.id, #tournamentDTO)")
    @PostMapping(value = "/tournaments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TournamentDTO> addNewTournament(@RequestBody TournamentDTO tournamentDto) {
        Tournament tournament = tournamentFactory.toEntity(tournamentDto);
        tournamentService.saveTournament(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentDto);
    }

    /**
     * Обрабатывает DELETE запрос для удаления турнира по ID.
     *
     * @param id идентификатор турнира, который нужно удалить
     * @return ResponseEntity<String>, сообщение об успешном удалении
     */
    @Operation(
            summary = "Удалить турнир",
            description = "Использовать для удаления турнира по id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tournamentService.isManager(authentication.principal.id, #id)")
    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<String> deleteTournament(@PathVariable long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.ok("Tournament with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает PUT запрос для добавления результата в турнир.
     *
     * @param tournamentId идентификатор турнира
     * @param resultId идентификатор результата
     * @return ResponseEntity<TournamentDTO>, обновленный TournamentDTO
     */
    @Operation(
            summary = "Добавить результат в турнир",
            description = "Использовать для добавления результата в турнир"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tournamentService.isManager(authentication.principal.id, #tournamentId)")
    @PutMapping("/tournaments/{tournamentId}/results/{resultId}")
    public ResponseEntity<TournamentDTO> addResultToTournament(@PathVariable long tournamentId, @PathVariable long resultId) {
        Tournament updatedTournament = tournamentService.addResultToTournament(tournamentId, resultId);
        return ResponseEntity.ok(tournamentFactory.toDTO(updatedTournament));
    }

    /**
     * Обрабатывает DELETE запрос для удаления результата из турнира.
     *
     * @param tournamentId идентификатор турнира
     * @param resultId идентификатор результата
     * @return ResponseEntity<TournamentDTO>, обновленный TournamentDTO
     */
    @Operation(
            summary = "Удалить результат из турнира",
            description = "Использовать для удаления результата из турнира"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tournamentService.isManager(authentication.principal.id, #tournamentId)")
    @DeleteMapping("/tournaments/{tournamentId}/results/{resultId}")
    public ResponseEntity<TournamentDTO> deleteResultFromTournament(@PathVariable long tournamentId, @PathVariable long resultId) {
        Tournament updatedTournament = tournamentService.deleteResultFromTournament(tournamentId, resultId);
        return ResponseEntity.ok(tournamentFactory.toDTO(updatedTournament));
    }

    /**
     * Обрабатывает GET запрос на получение всех результатов из турнира.
     *
     * @param tournamentId идентификатор турнира
     * @return ResponseEntity<List<FullResultDTO>>, содержащий все результаты из турнира
     */
    @Operation(
            summary = "Все результаты из турнира",
            description = "Использовать для получения всех результатов из турнира"
    )
    @GetMapping("/tournaments/{tournamentId}/results")
    public ResponseEntity<List<FullResultDTO>> allResultsFromTournament(@PathVariable long tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament.getResults().stream().map(x -> fullResultFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает PUT запрос для добавления команд и игроков в турнир.
     *
     * @param tournamentId идентификатор турнира
     * @param teamId идентификатор команды
     * @param playerId идентификатор игрока
     * @return ResponseEntity<TournamentDTO>, обновленный TournamentDTO
     */
    @Operation(
            summary = "Добавить команды и игроков в турнир",
            description = "Использовать для добавления команд и игроков в турнир"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tournamentService.isManager(authentication.principal.id, #tournamentId)")
    @PutMapping("/tournaments/{tournamentId}/teams/{teamId}/players/{playerId}")
    public ResponseEntity<TournamentDTO> addPlayersTeamsToTournament(@PathVariable long tournamentId, @PathVariable long teamId, @PathVariable long playerId) {
        Tournament updatedTournament = tournamentService.addTeamAndPlayerToTournament(tournamentId, teamId, playerId);
        return ResponseEntity.ok(tournamentFactory.toDTO(updatedTournament));
    }

    /**
     * Обрабатывает GET запрос на получение всех команд из турнира.
     *
     * @param tournamentId идентификатор турнира
     * @return ResponseEntity<List<TeamDTO>>, содержащий все команды из турнира
     */
    @Operation(
            summary = "Все команды из турнира",
            description = "Использовать для получения всех команд из турнира"
    )
    @GetMapping("/tournaments/{tournamentId}/teams")
    public ResponseEntity<List<TeamDTO>> allTeamsFromTournament(@PathVariable long tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament.getTeams().stream().map(x -> teamFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение всех игроков из турнира.
     *
     * @param tournamentId идентификатор турнира
     * @return ResponseEntity<List<PlayerDTO>>, содержащий всех игроков из турнира
     */
    @Operation(
            summary = "Все игроки из турнира",
            description = "Использовать для получения всех игроков из турнира"
    )
    @GetMapping("/tournaments/{tournamentId}/players")
    public ResponseEntity<List<PlayerDTO>> allPlayersFromTournament(@PathVariable long tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament.getPlayers().stream().map(x -> playerFactory.toDTO(x)).toList());
    }

    /* -------------------------------------------
                      Transfers
    ------------------------------------------- */

    /**
     * Обрабатывает GET запрос на получение всех трансферов.
     *
     * @return ResponseEntity<List<TransferDTO>>, содержащий все TransferDTO
     */
    @Operation(
            summary = "Получить все трансферы",
            description = "Использовать для получения всех трансферов"
    )
    @GetMapping("/transfers")
    public ResponseEntity<List<TransferDTO>> getTransfers() {
        return ResponseEntity.ok(transferService.getAllTransfers().stream().map(x -> transferFactory.toDTO(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение трансфера по ID.
     *
     * @param id идентификатор трансфера
     * @return ResponseEntity<TransferDTO>, содержащий данные запрашиваемого TransferDTO
     */
    @Operation(
            summary = "Получить трансфер по айди",
            description = "Использовать для получения трансфера по id"
    )
    @GetMapping("/transfers/{id}")
    public ResponseEntity<TransferDTO> getTransfer(@PathVariable long id) {
        return ResponseEntity.ok(transferFactory.toDTO(transferService.getTransfer(id)));
    }

    /**
     * Обрабатывает POST запрос для создания нового трансфера.
     *
     * @param transferDTO TransferDTO, который нужно создать
     * @return ResponseEntity<TransferDTO>, созданный TransferDTO
     */
    @Operation(
            summary = "Создать новый трансфер",
            description = "Использовать для создания нового трансфера"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @transferService.isManager(authentication.principal.id, #transferDTO)")
    @PostMapping("/transfers")
    public ResponseEntity<TransferDTO> addNewTransfer(@RequestBody TransferDTO transferDTO) {
        Transfer transfer = transferFactory.toEntity(transferDTO);
        transferService.saveTransfer(transfer);
        return ResponseEntity.status(HttpStatus.CREATED).body(transferDTO);
    }

    /**
     * Обрабатывает DELETE запрос для удаления трансфера по ID.
     *
     * @param id идентификатор трансфера, который нужно удалить
     * @return ResponseEntity<String>, сообщение об успешном удалении
     */
    @Operation(
            summary = "Удалить трансфер",
            description = "Использовать для удаления трансфера по id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @transferService.isManager(authentication.principal.id, #id)")
    @DeleteMapping("/transfers/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable long id) {
        transferService.deleteTransfer(id);
        return ResponseEntity.ok("Transfer with ID = " + id + " was deleted");
    }
}
