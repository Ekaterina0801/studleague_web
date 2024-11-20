package com.studleague.studleague.controllers;


import com.studleague.studleague.dto.FlagDTO;
import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.factory.FlagFactory;
import com.studleague.studleague.factory.TeamFactory;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.FlagService;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/flags")
public class FlagController {

    @Autowired
    public FlagService flagService;


    @Autowired
    public LeagueService leagueService;


    @Autowired
    public TeamService teamService;


    @Autowired
    public TeamFactory teamFactory;

    @Autowired
    private FlagFactory flagFactory;

    @Autowired
    private UserService userService;


    /**
     * Обрабатывает GET запрос на получение всех флагов.
     *
     * @return ResponseEntity<List < FlagDTO>>, содержащий все FlagDTO
     */
    @Operation(
            summary = "Получить все флаги",
            description = "Использовать для получения всех флагов"
    )
    @GetMapping
    public ResponseEntity<List<FlagDTO>> getFlags() {
        return ResponseEntity.ok(flagService.getAllFlags().stream().map(x -> flagFactory.mapToDto(x)).toList());
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
    @GetMapping("/{id}")
    public ResponseEntity<FlagDTO> getFlag(@PathVariable long id) {
        return ResponseEntity.ok(flagFactory.mapToDto(flagService.getFlagById(id)));
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
    @DeleteMapping("/{id}")
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
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or @flagService.isManager(authorization.principal.id, #flagDto)")
    public ResponseEntity<FlagDTO> addNewFlag(@RequestBody FlagDTO flagDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            authorities.forEach(authority -> {
                System.out.println("Granted Authority: " + authority.getAuthority());
            });
        }

        flagService.saveFlag(flagFactory.mapToEntity(flagDto));
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
    @DeleteMapping
    public ResponseEntity<String> deleteAllFlags() {
        flagService.deleteAllFlags();
        return ResponseEntity.ok("Flags were deleted");
    }

    /**
     * Обрабатывает GET запрос на получение команд по Flag.
     *
     * @param id идентификатор флага
     * @return ResponseEntity<List < TeamDTO>>, содержащий данные TeamDTO по флагу
     */
    @Operation(
            summary = "Получить флаг по айди",
            description = "Использовать для получения флага по id"
    )
    @GetMapping("/{id}/teams")
    public ResponseEntity<List<TeamDTO>> getTeamsByFlagId(@PathVariable long id) {
        return ResponseEntity.ok(teamService.getTeamsByFlagId(id).stream().map(x -> teamFactory.mapToDto(x)).toList());
    }

}
