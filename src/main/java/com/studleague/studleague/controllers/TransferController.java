package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.TransferDTO;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.factory.TransferFactory;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    public TransferService transferService;

    @Autowired
    public TransferFactory transferFactory;

    @Autowired
    private UserService userService;


    /**
     * Получить список трансферов с возможностью фильтрации и сортировки.
     *
     * @param playerId      идентификатор игрока для фильтрации трансферов.
     * @param oldTeamId     идентификатор предыдущей команды для фильтрации трансферов.
     * @param newTeamId     идентификатор новой команды для фильтрации трансферов.
     * @param leagueId      идентификатор лиги для фильтрации трансферов.
     * @param startDate     начальная дата диапазона трансферов (формат ISO: YYYY-MM-DD).
     * @param endDate       конечная дата диапазона трансферов (формат ISO: YYYY-MM-DD).
     * @param sortField     поле для сортировки, например: `player`, `transferDate`, `oldTeam`, `newTeam`.
     * @param sortDirection направление сортировки: `asc` (по возрастанию) или `desc` (по убыванию). По умолчанию — `asc`.
     * @return список объектов Transfer, соответствующих заданным критериям.
     */
    @Operation(
            summary = "Получить список трансферов",
            description = """
                    Возвращает список трансферов с возможностью фильтрации и сортировки.
                                    
                    Параметры фильтрации:
                    - `playerId`: идентификатор игрока для фильтрации.
                    - `oldTeamId`: идентификатор предыдущей команды для фильтрации.
                    - `newTeamId`: идентификатор новой команды для фильтрации.
                    - `leagueId`: идентификатор лиги для фильтрации. Учитываются обе команды — старая и новая.
                    - `startDate`: начальная дата диапазона трансферов (включительно).
                    - `endDate`: конечная дата диапазона трансферов (включительно).
                                    
                    Параметры сортировки:
                    - `sortField`: поле для сортировки (`player`, `transferDate`, `oldTeam`, `newTeam`).
                    - `sortDirection`: направление сортировки: `asc` (по возрастанию) или `desc` (по убыванию). По умолчанию используется `asc`.
                                    
                    Если параметры фильтрации или сортировки не указаны, возвращаются все трансферы без применения фильтров и сортировки.
                    """
    )
    @GetMapping
    public List<TransferDTO> getTransfers(
            @RequestParam(required = false) Long playerId,
            @RequestParam(required = false) Long oldTeamId,
            @RequestParam(required = false) Long newTeamId,
            @RequestParam(required = false) Long leagueId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
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

        return transferService.searchTransfers(playerId, oldTeamId, newTeamId, leagueId, startDate, endDate, sort).stream().map(x -> transferFactory.mapToDto(x)).toList();
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
    @GetMapping("/{id}")
    public ResponseEntity<TransferDTO> getTransfer(@PathVariable long id) {
        return ResponseEntity.ok(transferFactory.mapToDto(transferService.getTransfer(id)));
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
    @PostMapping
    public ResponseEntity<TransferDTO> addNewTransfer(@RequestBody TransferDTO transferDTO) {
        Transfer transfer = transferFactory.mapToEntity(transferDTO);
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable long id) {
        transferService.deleteTransfer(id);
        return ResponseEntity.ok("Transfer with ID = " + id + " was deleted");
    }


}
