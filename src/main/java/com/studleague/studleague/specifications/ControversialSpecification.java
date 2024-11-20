package com.studleague.studleague.specifications;

import com.studleague.studleague.entities.Controversial;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControversialSpecification {

    // Фильтр по номеру вопроса
    public static Specification<Controversial> questionNumberEquals(Integer questionNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("questionNumber"), questionNumber);
    }

    // Фильтр по статусу (множественный выбор)
    public static Specification<Controversial> statusIn(List<String> statuses) {
        return (root, query, criteriaBuilder) ->
                root.get("status").in(statuses);
    }

    // Фильтр по дате выпуска (issuedAt)
    public static Specification<Controversial> issuedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("issuedAt"), startDate, endDate);
    }

    // Фильтр по результату (FullResult)
    public static Specification<Controversial> fullResultEquals(Long fullResultId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("fullResult").get("id"), fullResultId);
    }

    // Комплексный поиск
    public static Specification<Controversial> searchControversials(Integer questionNumber, List<String> statuses, LocalDateTime startDate, LocalDateTime endDate, Long fullResultId) {
        Specification<Controversial> spec = Specification.where(null);

        if (questionNumber != null) {
            spec = spec.and(questionNumberEquals(questionNumber));
        }

        if (statuses != null && !statuses.isEmpty()) {
            spec = spec.and(statusIn(statuses));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(issuedAtBetween(startDate, endDate));
        }

        if (fullResultId != null) {
            spec = spec.and(fullResultEquals(fullResultId));
        }

        return spec;
    }


    public static Sort sortBy(List<String> sortBy, List<String> sortOrder) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sortBy != null && !sortBy.isEmpty()) {
            for (int i = 0; i < sortBy.size(); i++) {
                String field = sortBy.get(i);
                String order = (sortOrder != null && sortOrder.size() > i) ? sortOrder.get(i) : "asc"; // По умолчанию сортировка по возрастанию
                orders.add(new Sort.Order(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, field));
            }
        }

        return Sort.by(orders);
    }
}

