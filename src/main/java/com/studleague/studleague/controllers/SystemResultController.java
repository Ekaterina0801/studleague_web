package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.systemResult.SystemResultDTO;
import com.studleague.studleague.entities.SystemResult;
import com.studleague.studleague.mappers.systemResult.SystemResultMapper;
import com.studleague.studleague.services.interfaces.SystemResultService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system-results")
public class SystemResultController {

    @Autowired
    SystemResultService systemResultService;

    @Autowired
    SystemResultMapper systemResultMapper;

    @Autowired
    TeamCompositionService teamCompositionService;

    /**
     * Обрабатывает GET запрос на получение всех систем подсчета результатов лиги.
     *
     * @return ResponseEntity<List < SystemResultDTO>>, содержащий все SystemResultDTO
     */
    @Operation(
            summary = "Получить все системы результатов лиг",
            description = "Использовать для получения всех систем подсчета результатов лиги"
    )
    @GetMapping
    public ResponseEntity<List<SystemResultDTO>> getSystemResults() {
        return ResponseEntity.ok(systemResultService.findAll().stream().map(x -> systemResultMapper.mapToDto(x)).toList());
    }

    /**
     * Обрабатывает GET запрос на получение системы результата по ID.
     *
     * @param id идентификатор системы подсчета результатов лиги
     * @return ResponseEntity<SystemResultDTO>, содержащий данные запрашиваемого SystemResultDTO
     */
    @Operation(
            summary = "Получить систему подсчета результатов по айди",
            description = "Использовать для получения системы подсчета результатов по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<SystemResultDTO> getSystemResult(@PathVariable long id) {
        return ResponseEntity.ok(systemResultMapper.mapToDto(systemResultService.findById(id)));
    }

    /**
     * Обрабатывает POST запрос для создания новой системы результатов.
     *
     * @param systemResultDTO SystemResultDTO, который нужно создать
     * @return ResponseEntity<SystemResultDTO>, созданный SystemResultDTO
     */
    @Operation(
            summary = "Создать новую систему результатов",
            description = "Использовать для создания новой системы результатов"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<SystemResultDTO> addNewSystemResult(@RequestBody SystemResultDTO systemResultDTO) {
        SystemResult systemResult = systemResultMapper.mapToEntity(systemResultDTO);
        systemResultService.save(systemResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(systemResultDTO);
    }

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
    public ResponseEntity<String> deleteSystemResult(@PathVariable long id) {
        systemResultService.deleteById(id);
        return ResponseEntity.ok("SystemResult with ID = " + id + " was deleted");
    }


}
