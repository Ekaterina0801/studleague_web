package com.studleague.studleague.specifications;

import com.studleague.studleague.entities.Team;
import jakarta.persistence.criteria.Join;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public class TeamSpecification {

    public static Specification<Team> teamNameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("teamName")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Team> leagueIdEquals(Long leagueId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("league").get("id"), leagueId);
    }

    public static Specification<Team> flagsIn(List<Long> flagIds) {
        return (root, query, criteriaBuilder) -> {
            if (flagIds == null || flagIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> flagsJoin = root.join("flags");
            return flagsJoin.get("id").in(flagIds);
        };
    }

    public static Specification<Team> searchTeams(String name, Long leagueId, List<Long> flagIds, Sort sort) {
        Specification<Team> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and(teamNameContains(name));
        }

        if (leagueId != null) {
            spec = spec.and(leagueIdEquals(leagueId));
        }

        if (flagIds != null && !flagIds.isEmpty()) {
            spec = spec.and(flagsIn(flagIds));
        }

        Specification<Team> finalSpec = spec;
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


