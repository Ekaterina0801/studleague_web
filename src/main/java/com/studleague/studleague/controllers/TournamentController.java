package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.ControversialDTO;
import com.studleague.studleague.dto.TournamentDTO;
import com.studleague.studleague.dto.TournamentMainInfoDTO;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.mappers.ControversialMapper;
import com.studleague.studleague.mappers.TournamentMainInfoMapper;
import com.studleague.studleague.mappers.TournamentMapper;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.ControversialService;
import com.studleague.studleague.services.interfaces.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    @Autowired
    public TournamentService tournamentService;

    @Autowired
    public TournamentMapper tournamentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private TournamentMainInfoMapper tournamentMainInfoMapper;

    @Autowired
    private ControversialService controversialService;

    @Autowired
    private ControversialMapper controversialMapper;

    private static final Logger logger = LoggerFactory.getLogger(TournamentController.class);


    /**
     * Получить список турниров с возможностью фильтрации и сортировки.
     *
     * @param name          название турнира (подстрочный поиск).
     * @param leagueId      идентификатор лиги для фильтрации турниров.
     * @param startDate     начальная дата турнира (фильтрация по дате начала).
     * @param endDate       конечная дата турнира (фильтрация по дате окончания).
     * @param sortField     поле для сортировки, например: `name`, `startDate`, `endDate`.
     * @param sortDirection направление сортировки: `asc` (по возрастанию) или `desc` (по убыванию). По умолчанию — `asc`.
     * @return список объектов Tournament, соответствующих заданным критериям.
     */
    @Operation(
            summary = "Получить список турниров",
            description = """
                    Возвращает список турниров с возможностью фильтрации и сортировки.
                                    
                    Параметры фильтрации:
                    - `name`: название турнира (частичное совпадение).
                    - `leagueId`: идентификатор лиги для фильтрации турниров.
                    - `startDate`: начальная дата турнира.
                    - `endDate`: конечная дата турнира.
                                    
                    Параметры сортировки:
                    - `sortField`: поле для сортировки (`name`, `startDate`, `endDate`).
                    - `sortDirection`: направление сортировки: `asc` (по возрастанию) или `desc` (по убыванию). По умолчанию используется `asc`.
                                    
                    Если параметры фильтрации или сортировки не указаны, возвращаются все турниры без применения фильтров и сортировки.
                    """
    )
    @GetMapping
    public Page<TournamentMainInfoDTO> getTournaments(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long leagueId,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            Pageable pageable
    ) {
        Sort sort = null;
        if (sortField != null && !sortField.isEmpty()) {
            sort = Sort.by(
                    "asc".equalsIgnoreCase(sortDirection)
                            ? Sort.Order.asc(sortField)
                            : Sort.Order.desc(sortField)
            );
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        Page<Tournament> tournamentPage = tournamentService.searchTournaments(name, leagueId, startDate, endDate, sort, pageable);
        return tournamentPage.map(tournamentMainInfoMapper::mapToDto);
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
    @GetMapping("/{id}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable long id) {
        return ResponseEntity.ok(tournamentMapper.mapToDto(tournamentService.getTournamentById(id)));
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TournamentDTO> addNewTournament(@RequestBody TournamentDTO tournamentDto) {
        Tournament tournament = tournamentMapper.mapToEntity(tournamentDto);
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTournament(@PathVariable long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.ok("Tournament with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает PUT запрос для добавления результата в турнир.
     *
     * @param tournamentId идентификатор турнира
     * @param resultId     идентификатор результата
     * @return ResponseEntity<TournamentDTO>, обновленный TournamentDTO
     */
    @Operation(
            summary = "Добавить результат в турнир",
            description = "Использовать для добавления результата в турнир"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tournamentService.isManager(authentication.principal.id, #tournamentId)")
    @PutMapping("/{tournamentId}/results/{resultId}")
    public ResponseEntity<TournamentDTO> addResultToTournament(@PathVariable long tournamentId, @PathVariable long resultId) {
        Tournament updatedTournament = tournamentService.addResultToTournament(tournamentId, resultId);
        return ResponseEntity.ok(tournamentMapper.mapToDto(updatedTournament));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @tournamentService.isManager(authentication.principal.id, #tournamentId)")
    @PutMapping("/{tournamentId}/teams/{teamId}")
    public ResponseEntity<TournamentDTO> addTeamToTournament(@PathVariable long tournamentId, @PathVariable long teamId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Authentication Principal: {}", authentication.getPrincipal());
        Tournament updatedTournament = tournamentService.addTeamToTournament(tournamentId, teamId);
        return ResponseEntity.ok(tournamentMapper.mapToDto(updatedTournament));
    }


    /**
     * Обрабатывает DELETE запрос для удаления результата из турнира.
     *
     * @param tournamentId идентификатор турнира
     * @param resultId     идентификатор результата
     * @return ResponseEntity<TournamentDTO>, обновленный TournamentDTO
     */
    @Operation(
            summary = "Удалить результат из турнира",
            description = "Использовать для удаления результата из турнира"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tournamentService.isManager(authentication.principal.id, #tournamentId)")
    @DeleteMapping("/{tournamentId}/results/{resultId}")
    public ResponseEntity<TournamentDTO> deleteResultFromTournament(@PathVariable long tournamentId, @PathVariable long resultId) {
        Tournament updatedTournament = tournamentService.deleteResultFromTournament(tournamentId, resultId);
        return ResponseEntity.ok(tournamentMapper.mapToDto(updatedTournament));
    }


    /**
     * Обрабатывает PUT запрос для добавления команд и игроков в турнир.
     *
     * @param tournamentId идентификатор турнира
     * @param teamId       идентификатор команды
     * @param playerId     идентификатор игрока
     * @return ResponseEntity<TournamentDTO>, обновленный TournamentDTO
     */
    @Operation(
            summary = "Добавить команды и игроков в турнир",
            description = "Использовать для добавления команд и игроков в турнир"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tournamentService.isManager(authentication.principal.id, #tournamentId)")
    @PutMapping("/{tournamentId}/teams/{teamId}/players/{playerId}")
    public ResponseEntity<TournamentDTO> addPlayersTeamsToTournament(@PathVariable long tournamentId, @PathVariable long teamId, @PathVariable long playerId) {
        Tournament updatedTournament = tournamentService.addTeamAndPlayerToTournament(tournamentId, teamId, playerId);
        return ResponseEntity.ok(tournamentMapper.mapToDto(updatedTournament));
    }

    @GetMapping("/{tournamentId}/controversials")
    public ResponseEntity<List<ControversialDTO>> getControversialsByTournamentId(@PathVariable long tournamentId) {
        return ResponseEntity.ok(controversialService.getControversialsByTournamentId(tournamentId).stream().map(x -> controversialMapper.mapToDto(x)).collect(Collectors.toList()));
    }

}
