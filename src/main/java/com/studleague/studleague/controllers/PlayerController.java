package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.player.PlayerCreationDTO;
import com.studleague.studleague.dto.player.PlayerDTO;
import com.studleague.studleague.dto.player.PlayerMainInfoDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.mappers.player.PlayerCreationMapper;
import com.studleague.studleague.mappers.player.PlayerMainInfoMapper;
import com.studleague.studleague.mappers.player.PlayerMapper;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    public PlayerService playerService;

    @Autowired
    public PlayerMapper playerMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerMainInfoMapper playerMainInfoMapper;

    @Autowired
    private PlayerCreationMapper playerCreationMapper;

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
    public Page<PlayerMainInfoDTO> getPlayers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String patronymic,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) LocalDate bornBefore,
            @RequestParam(required = false) LocalDate bornAfter,
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
        Page<Player> playerPage = playerService.searchPlayers(name, patronymic, surname, teamId, bornBefore, bornAfter, sort, pageable);
        return playerPage.map(playerMainInfoMapper::mapToDto);
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
        return ResponseEntity.ok(playerMapper.mapToDto(playerService.getPlayerById(id)));
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
    public ResponseEntity<PlayerCreationDTO> addNewPlayer(@RequestBody PlayerCreationDTO playerDTO) {
        playerService.savePlayer(playerCreationMapper.mapToEntity(playerDTO));
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
