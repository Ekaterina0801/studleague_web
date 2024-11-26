package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.factory.TeamFactory;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    public TeamService teamService;

    @Autowired
    public TeamFactory teamFactory;

    @Autowired
    private UserService userService;


    /**
     * Обрабатывает GET запрос на поиск команд.
     *
     * @return Список объектов Team, соответствующих заданным критериям.
     */
    @Operation(
            summary = "Поиск команд",
            description = """
                    Возвращает список команд с возможностью фильтрации и сортировки.
                                        
                    Параметры фильтрации:
                    - `name` — фильтрация по названию команды (частичное совпадение).
                    - `leagueId` — идентификатор лиги для фильтрации команд, относящихся к указанной лиге.
                    - `flagIds` — список идентификаторов флагов для фильтрации команд, связанных с указанными флагами.
                                        
                    Параметры сортировки:
                    - `sortField` — поле, по которому требуется выполнить сортировку. Примеры: `name`, `league`, `createdDate`.
                    - `sortDirection` — направление сортировки: `asc` (по возрастанию) или `desc` (по убыванию). По умолчанию используется `asc`.
                                        
                    Если параметры фильтрации или сортировки не указаны, возвращаются все команды без применения фильтров и сортировки.
                    """
    )
    @GetMapping
    public Page<TeamDTO> getTeams(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long leagueId,
            @RequestParam(required = false) List<Long> flagIds,
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

        Page<Team> teamPage = teamService.searchTeams(name, leagueId, flagIds, sort, pageable);

        return teamPage.map(teamFactory::mapToDto);
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
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeam(@PathVariable long id) {
        return ResponseEntity.ok(teamFactory.mapToDto(teamService.getTeamById(id)));
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
    @GetMapping("/{id}/results")
    public ResponseEntity<List<InfoTeamResults>> getTeamResults(@PathVariable long id) {
        List<InfoTeamResults> results = teamService.getInfoTeamResultsByTeam(id);
        return ResponseEntity.ok(teamService.getInfoTeamResultsByTeam(id));
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDTO> addNewTeam(@RequestBody TeamDTO teamDTO) {
        Team team = teamFactory.mapToEntity(teamDTO);
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok("Team with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает PUT запрос для добавления игрока в команду.
     *
     * @param teamId   идентификатор команды
     * @param playerId идентификатор игрока
     * @return ResponseEntity<TeamDTO>, обновленная TeamDTO
     */
    @Operation(
            summary = "Добавить игрока в команду",
            description = "Использовать для добавления игрока в команду"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @teamService.isManager(authentication.principal.id, #teamId)")
    @PutMapping("/players/{playerId}")
    public ResponseEntity<TeamDTO> addPlayerToTeam(@PathVariable long teamId, @PathVariable long playerId) {
        Team updatedTeam = teamService.addPlayerToTeam(teamId, playerId);
        return ResponseEntity.ok(teamFactory.mapToDto(updatedTeam));
    }

    /**
     * Обрабатывает DELETE запрос для удаления игрока из команды.
     *
     * @param teamId   идентификатор команды
     * @param playerId идентификатор игрока
     * @return ResponseEntity<TeamDTO>, обновленная TeamDTO
     */
    @Operation(
            summary = "Удалить игрока из команды",
            description = "Использовать для удаления игрока из команды"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @teamService.isManager(authentication.principal.id, #teamId)")
    @DeleteMapping("/players/{playerId}")
    public ResponseEntity<TeamDTO> deletePlayerFromTeam(@PathVariable long teamId, @PathVariable long playerId) {
        Team updatedTeam = teamService.deletePlayerFromTeam(teamId, playerId);
        return ResponseEntity.ok(teamFactory.mapToDto(updatedTeam));
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
    @PutMapping("/flags/{flagId}")
    public ResponseEntity<TeamDTO> addFlagToTeam(@PathVariable long teamId, @PathVariable long flagId) {
        Team updatedTeam = teamService.addFlagToTeam(teamId, flagId);
        return ResponseEntity.ok(teamFactory.mapToDto(updatedTeam));
    }

    /**
     * Обрабатывает PUT запрос для добавления лиги в команду.
     *
     * @param teamId   идентификатор команды
     * @param leagueId идентификатор лиги
     * @return ResponseEntity<TeamDTO>, обновленная TeamDTO
     */
    @Operation(
            summary = "Добавить лигу в команду",
            description = "Использовать для добавления лиги в команду"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @leagueService.isManager(authentication.principal.id, #leagueId)")
    @PutMapping("/leagues/{leagueId}")
    public ResponseEntity<TeamDTO> addLeagueToTeam(@PathVariable long teamId, @PathVariable long leagueId) {
        Team updatedTeam = teamService.addLeagueToTeam(teamId, leagueId);
        return ResponseEntity.ok(teamFactory.mapToDto(updatedTeam));
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
    @DeleteMapping("/flags/{flagId}")
    public ResponseEntity<TeamDTO> deleteFlagFromTeam(@PathVariable long teamId, @PathVariable long flagId) {
        Team updatedTeam = teamService.deleteFlagFromTeam(teamId, flagId);
        return ResponseEntity.ok(teamFactory.mapToDto(updatedTeam));
    }


}
