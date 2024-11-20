package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.factory.PlayerFactory;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    public PlayerService playerService;

    @Autowired
    public PlayerFactory playerFactory;

    @Autowired
    private UserService userService;

    /**
     * Обрабатывает GET запрос на получение всех игроков.
     *
     * @return ResponseEntity<List < PlayerDTO>>, содержащий все PlayerDTO
     */
    @Operation(
            summary = "Получить список игроков",
            description = """
                    Возвращает список игроков с возможностью фильтрации и сортировки.
                                        
                    Параметры фильтрации:
                    - `name` — имя игрока (частичное совпадение).
                    - `surname` — фамилия игрока (частичное совпадение).
                    - `teamId` — идентификатор команды для фильтрации игроков, принадлежащих конкретной команде.
                    - `bornBefore` — дата, до которой родились игроки.
                    - `bornAfter` — дата, после которой родились игроки.
                                        
                    Параметры сортировки:
                    - `sortBy` — список полей, по которым требуется сортировка (например, `name`, `surname`, `dateOfBirth`).
                    - `sortOrder` — порядок сортировки (asc или desc) для соответствующих полей.
                                        
                    Если параметры фильтрации или сортировки не указаны, будут возвращены все записи без фильтрации в порядке по умолчанию.
                    """
    )
    @GetMapping
    public List<PlayerDTO> getPlayers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) LocalDate bornBefore,
            @RequestParam(required = false) LocalDate bornAfter,
            @RequestParam(required = false) List<String> sortBy,
            @RequestParam(required = false) List<String> sortOrder
    ) {
        return playerService.searchPlayers(name, surname, teamId, bornBefore, bornAfter, sortBy, sortOrder).stream().map(x -> playerFactory.mapToDto(x)).toList();
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
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable long id) {
        return ResponseEntity.ok(playerFactory.mapToDto(playerService.getPlayerById(id)));
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
    @PostMapping
    public ResponseEntity<PlayerDTO> addNewPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = playerFactory.mapToEntity(playerDTO);
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok("Player with ID = " + id + " was deleted");
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
    @DeleteMapping
    public ResponseEntity<String> deleteAllPlayers() {
        playerService.deleteAllPlayers();
        return ResponseEntity.ok("Players were deleted");
    }

}
