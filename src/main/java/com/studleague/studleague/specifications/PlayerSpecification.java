package com.studleague.studleague.specifications;

import com.studleague.studleague.entities.Player;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PlayerSpecification {


    public static Specification<Player> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Player> patronymicContains(String patronymic) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("patronymic")), "%" + patronymic.toLowerCase() + "%");
    }


    public static Specification<Player> surnameContains(String surname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
    }


    public static Specification<Player> belongsToTeam(Long teamId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
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


    public static Specification<Player> searchPlayers(String name, String surname, Long teamId, LocalDate bornBefore, LocalDate bornAfter, Sort sort) {
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

        Specification<Player> finalSpec = spec;
        return (root, query, criteriaBuilder) -> {
            if (sort != null) {
                query.orderBy(sort.stream()
                        .map(order -> order.isAscending()
                                ? criteriaBuilder.asc(root.get(order.getProperty()))
                                : criteriaBuilder.desc(root.get(order.getProperty())))
                        .toList());
            }
            return finalSpec.toPredicate(root, query, criteriaBuilder);
        };
    }
}

