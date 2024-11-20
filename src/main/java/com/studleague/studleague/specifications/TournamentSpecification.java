package com.studleague.studleague.specifications;

import com.studleague.studleague.entities.Tournament;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TournamentSpecification {

    public static Specification<Tournament> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Tournament> leagueIdEquals(Long leagueId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("leagues").get("id"), leagueId);
    }

    public static Specification<Tournament> startDateAfter(LocalDateTime startDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfStart"), startDate);
    }

    public static Specification<Tournament> endDateBefore(LocalDateTime endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("dateOfEnd"), endDate);
    }

    public static Specification<Tournament> searchTournaments(String name, Long leagueId, LocalDateTime startDate, LocalDateTime endDate, Sort sort) {
        Specification<Tournament> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and(nameContains(name));
        }

        if (leagueId != null) {
            spec = spec.and(leagueIdEquals(leagueId));
        }

        if (startDate != null) {
            spec = spec.and(startDateAfter(startDate));
        }

        if (endDate != null) {
            spec = spec.and(endDateBefore(endDate));
        }

        Specification<Tournament> finalSpec = spec;
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


