package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.factory.ControversialFactory;
import com.studleague.studleague.factory.FullResultFactory;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    public ResultService resultService;

    @Autowired
    public FullResultFactory fullResultFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private ControversialFactory controversialFactory;

    /**
     * Обрабатывает GET запрос на получение всех результатов.
     *
     * @return ResponseEntity<List < FullResultDTO>>, содержащий все FullResultDTO
     */
    @Operation(
            summary = "Получить все результаты",
            description = "Использовать для получения всех результатов"
    )
    @GetMapping
    public ResponseEntity<List<FullResultDTO>> getResults() {
        return ResponseEntity.ok(resultService.getAllFullResults().stream().map(x -> fullResultFactory.mapToDto(x)).toList());
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
    @GetMapping("/{id}")
    public ResponseEntity<FullResultDTO> getResult(@PathVariable long id) {
        return ResponseEntity.ok(fullResultFactory.mapToDto(resultService.getFullResultById(id)));
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
    @PostMapping
    public ResponseEntity<FullResultDTO> addNewResult(@RequestBody FullResultDTO fullResultDTO) {
        if (fullResultDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        FullResult fullResult = fullResultFactory.mapToEntity(fullResultDTO);
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFullResult(@PathVariable long id) {
        resultService.deleteFullResult(id);
        return ResponseEntity.ok("FullResult with ID = " + id + " was deleted");
    }

    /**
     * Обрабатывает PUT запрос для добавления спорного в результат.
     *
     * @param resultId        идентификатор результата
     * @param controversialId идентификатор спорного
     * @return ResponseEntity<FullResultDTO>, обновленный FullResultDTO
     */
    @Operation(
            summary = "Добавить спорный в результат",
            description = "Использовать для добавления спорного в результат"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or @resultService.isManager(authentication.principal.id, #resultId)")
    @PutMapping("/{resultId}/controversials/{controversialId}")
    public ResponseEntity<FullResultDTO> addControversialToResult(@PathVariable long resultId, @PathVariable long controversialId) {
        FullResult fullResult = resultService.addControversialToResult(resultId, controversialId);
        return ResponseEntity.ok(fullResultFactory.mapToDto(fullResult));
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
    @DeleteMapping
    public ResponseEntity<String> deleteAllResults() {
        resultService.deleteAllResults();
        return ResponseEntity.ok("Results were deleted");
    }

}
