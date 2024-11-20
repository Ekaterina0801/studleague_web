package com.studleague.studleague.specifications;

import com.studleague.studleague.entities.Player;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlayerSpecification {


    public static Specification<Player> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }


    public static Specification<Player> surnameContains(String surname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
    }


    public static Specification<Player> belongsToTeam(Long teamId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true); // Для устранения дубликатов
            return criteriaBuilder.equal(
                    root.join("teams").get("id"), teamId
            );
        };
    }


    public static Specification<Player> bornBefore(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("dateOfBirth"), date);
    }


    public static Specification<Player> bornAfter(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"), date);
    }


    public static Specification<Player> searchPlayers(String name, String surname, Long teamId, LocalDate bornBefore, LocalDate bornAfter) {
        Specification<Player> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and(nameContains(name));
        }

        if (surname != null && !surname.isEmpty()) {
            spec = spec.and(surnameContains(surname));
        }

        if (teamId != null) {
            spec = spec.and(belongsToTeam(teamId));
        }

        if (bornBefore != null) {
            spec = spec.and(bornBefore(bornBefore));
        }

        if (bornAfter != null) {
            spec = spec.and(bornAfter(bornAfter));
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

