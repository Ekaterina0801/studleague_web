package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.ControversialDTO;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.factory.ControversialFactory;
import com.studleague.studleague.services.interfaces.ControversialService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/controversials")
public class ControversialController {

    @Autowired
    public ControversialFactory controversialFactory;

    @Autowired
    public ControversialService controversialService;


    /**
     * Обрабатывает GET запрос на получение всех Controversial.
     *
     * @return ResponseEntity<List < ControversialDTO>>, содержащий все ControversialDTO
     */
    /**
     * Обрабатывает GET запрос на получение всех Controversial.
     *
     * @return ResponseEntity<List < ControversialDTO>>, содержащий все ControversialDTO
     */
    @Operation(
            summary = "Получить список спорных записей",
            description = """
                    Возвращает список спорных записей с возможностью фильтрации и сортировки.
                                        
                    Параметры фильтрации:
                    - `questionNumber` — фильтрация по номеру вопроса.
                    - `statuses` — список статусов для фильтрации записей.
                    - `startDate` и `endDate` — диапазон дат для фильтрации записей.
                    - `fullResultId` — фильтрация по идентификатору полного результата.
                                        
                    Параметры сортировки:
                    - `sortBy` — список полей, по которым требуется сортировка.
                    - `sortOrder` — порядок сортировки (asc или desc) для соответствующих полей.
                                        
                    Если параметры фильтрации или сортировки не указаны, будут возвращены все записи без фильтрации в порядке по умолчанию.
                    """
    )
    @GetMapping
    public List<ControversialDTO> getControversials(
            @RequestParam(required = false) Integer questionNumber,
            @RequestParam(required = false) List<String> statuses,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(required = false) Long fullResultId,
            @RequestParam(required = false) List<String> sortBy,
            @RequestParam(required = false) List<String> sortOrder
    ) {

        return controversialService.searchControversials(questionNumber, statuses, startDate, endDate, fullResultId, sortBy, sortOrder).stream().map(x -> controversialFactory.mapToDto(x)).toList();
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
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or @controversialService.isManager(authentication.principal.id, #controversialDto)")
    public ResponseEntity<ControversialDTO> addNewControversial(@RequestBody ControversialDTO controversialDto) {
        Controversial controversial = controversialFactory.mapToEntity(controversialDto);
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
    @GetMapping("/{id}")
    public ResponseEntity<ControversialDTO> controversialById(@PathVariable long id) {
        Controversial controversial = controversialService.getControversialById(id);
        ControversialDTO controversialDTO = controversialFactory.mapToDto(controversial);
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
    @DeleteMapping("/{id}")
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
    @DeleteMapping
    public ResponseEntity<String> deleteAllControversials() {
        controversialService.deleteAllControversials();
        return ResponseEntity.ok("Controversials were deleted");
    }

}
