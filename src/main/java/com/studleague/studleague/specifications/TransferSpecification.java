package com.studleague.studleague.specifications;

import com.studleague.studleague.entities.Transfer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TransferSpecification {

    public static Specification<Transfer> playerEquals(Long playerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("player").get("id"), playerId);
    }

    public static Specification<Transfer> oldTeamEquals(Long oldTeamId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("oldTeam").get("id"), oldTeamId);
    }

    public static Specification<Transfer> newTeamEquals(Long newTeamId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("newTeam").get("id"), newTeamId);
    }

    public static Specification<Transfer> leagueEquals(Long leagueId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("oldTeam").get("league").get("id"), leagueId),
                        criteriaBuilder.equal(root.get("newTeam").get("league").get("id"), leagueId)
                );
    }

    public static Specification<Transfer> transferDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("transferDate"), startDate, endDate);
    }

    public static Specification<Transfer> searchTransfers(Long playerId, Long oldTeamId, Long newTeamId, Long leagueId, LocalDate startDate, LocalDate endDate, Sort sort) {
        Specification<Transfer> spec = Specification.where(null);

        if (playerId != null) {
            spec = spec.and(playerEquals(playerId));
        }
        if (oldTeamId != null) {
            spec = spec.and(oldTeamEquals(oldTeamId));
        }
        if (newTeamId != null) {
            spec = spec.and(newTeamEquals(newTeamId));
        }
        if (leagueId != null) {
            spec = spec.and(leagueEquals(leagueId));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and(transferDateBetween(startDate, endDate));
        }

        Specification<Transfer> finalSpec = spec;
        return (root, query, criteriaBuilder) -> {
            // Добавляем сортировку, если она указана
            if (sort != null) {
                query.orderBy(sort.stream()
                        .map(order -> order.isAscending()
                                ? criteriaBuilder.asc(root.get(order.getProperty()))
                                : criteriaBuilder.desc(root.get(order.getProperty())))
                        .toList());
            }
            // Возвращаем общий предикат
            return finalSpec.toPredicate(root, query, criteriaBuilder);
        };
    }
}

