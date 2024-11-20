package com.studleague.studleague.specifications;

import com.studleague.studleague.entities.TeamComposition;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class TeamCompositionSpecification {

    public static Specification<TeamComposition> parentTeamEquals(Long teamId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("parentTeam").get("id"), teamId);
    }

    public static Specification<TeamComposition> tournamentEquals(Long tournamentId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("tournament").get("id"), tournamentId);
    }

    public static Specification<TeamComposition> searchTeamCompositions(Long teamId, Long tournamentId, Sort sort) {
        Specification<TeamComposition> spec = Specification.where(null);

        if (teamId != null) {
            spec = spec.and(parentTeamEquals(teamId));
        }

        if (tournamentId != null) {
            spec = spec.and(tournamentEquals(tournamentId));
        }

        Specification<TeamComposition> finalSpec = spec;
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


