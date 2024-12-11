package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.TeamCompositionDTO;
import com.studleague.studleague.mappers.TeamCompositionMapper;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-compositions")
public class TeamCompositionController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamCompositionService teamCompositionService;

    @Autowired
    private TeamCompositionMapper teamCompositionMapper;

    /**
     * Обрабатывает DELETE запрос для удаления системы подсчета результатов по ID.
     *
     * @param id идентификатор системы подсчета результатов, который нужно удалить
     * @return ResponseEntity<String>, сообщение об успешном удалении
     */
    @Operation(
            summary = "Удалить систему подсчета результатов",
            description = "Использовать для удаления системы подсчета результатов по id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeamComposition(@PathVariable long id) {
        teamCompositionService.deleteById(id);
        return ResponseEntity.ok("SystemResult with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает GET запрос на получение всех составов команд.
     *
     * @return Список объектов TeamComposition с поддержкой фильтрации и сортировки.
     */
    @Operation(
            summary = "Получить список составов команд",
            description = """
                    Возвращает список составов команд с возможностью фильтрации и сортировки.
                                        
                    Параметры фильтрации:
                    - `teamId` — идентификатор команды для фильтрации составов, связанных с определенной командой.
                    - `tournamentId` — идентификатор турнира для фильтрации составов, связанных с определенным турниром.
                                        
                    Параметры сортировки:
                    - `sortField` — поле, по которому требуется выполнить сортировку. Примеры: `parentTeam`, `tournament`.
                    - `sortDirection` — направление сортировки: `asc` (по возрастанию) или `desc` (по убыванию). По умолчанию используется `asc`.
                                        
                    Если параметры фильтрации или сортировки не указаны, возвращаются все записи без фильтрации и сортировки.
                    """
    )
    @GetMapping
    public List<TeamCompositionDTO> getTeamCompositions(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) Long tournamentId,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Sort sort = null;
        if (sortField != null && !sortField.isEmpty()) {
            sort = Sort.by(
                    "asc".equalsIgnoreCase(sortDirection)
                            ? Sort.Order.asc(sortField)
                            : Sort.Order.desc(sortField)
            );
        }

        return teamCompositionService.searchTeamCompositions(teamId, tournamentId, sort).stream().map(x -> teamCompositionMapper.mapToDto(x)).toList();
    }

}
