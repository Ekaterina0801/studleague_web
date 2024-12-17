package com.studleague.studleague.mappers;

import com.studleague.studleague.entities.*;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MappingUtils {

    private final EntityRetrievalUtils entityRetrievalUtils;

    //@Lazy
    //private final ResultMainInfoMapper resultMainInfoMapper;

    // ------------------------------------ Tournament Mapping ------------------------------------
    @Named("tournamentIdToTournament")
    Tournament mapTournamentIdToTournament(Long id) {
        return entityRetrievalUtils.getTournamentOrThrow(id);
    }

    @Named("tournamentIdsToTournaments")
    List<Tournament> mapTournamentIdsToTournaments(List<Long> tournamentIds) {
        return toEntities(tournamentIds, entityRetrievalUtils::getTournamentOrThrow);
    }

    @Named("tournamentsToTournamentIds")
    List<Long> mapTournamentsToTournamentIds(List<Tournament> tournaments) {
        return toIds(tournaments, Tournament::getId);
    }

    // ------------------------------------ Team Mapping ------------------------------------
    @Named("teamIdToTeam")
    Team mapTeamIdToTeam(Long teamId) {
        return entityRetrievalUtils.getTeamOrThrow(teamId);
    }

    @Named("teamIdsToTeams")
    List<Team> mapTeamIdsToTeams(List<Long> teamIds) {
        return toEntities(teamIds, entityRetrievalUtils::getTeamOrThrow);
    }

    @Named("teamsToTeamIds")
    List<Long> mapTeamsToTeamIds(List<Team> teams) {
        return toIds(teams, Team::getId);
    }

    // ------------------------------------ League Mapping ------------------------------------
    @Named("leagueIdToLeague")
    League leagueIdToLeague(Long leagueId) {
        return entityRetrievalUtils.getLeagueOrThrow(leagueId);
    }


    @Named("leaguesIdsToEntities")
    List<League> mapLeaguesIdsToEntities(List<Long> leagueIds) {
        return toEntities(leagueIds, entityRetrievalUtils::getLeagueOrThrow);
    }

    @Named("leaguesToIds")
    List<Long> mapLeaguesToIds(List<League> leagues) {
        return toIds(leagues, League::getId);
    }

    // ------------------------------------ User Mapping ------------------------------------
    @Named("userIdToUser")
    User mapUserIdToUser(Long userId) {
        return entityRetrievalUtils.getUserOrThrow(userId);
    }

    @Named("userIdsToManagers")
    List<User> mapUserIdsToManagers(List<Long> userIds) {
        return toEntities(userIds, entityRetrievalUtils::getUserOrThrow);
    }

    @Named("managersToUserIds")
    List<Long> mapManagersToUserIds(List<User> managers) {
        return toIds(managers, User::getId);
    }

    // ------------------------------------ Player Mapping ------------------------------------
    @Named("playerIdToPlayer")
    Player mapPlayerIdToPlayer(Long playerId) {
        return entityRetrievalUtils.getPlayerOrThrow(playerId);
    }

    // ------------------------------------ Result Mapping ------------------------------------
//    @Named("toEntityResultIfExists")
//    FullResult toEntityIfExists(ResultMainInfoDTO dto) {
//        return dto != null ? resultMainInfoMapper.mapToEntity(dto) : null;
//    }

    @Named("systemResultIdToSystemResult")
    SystemResult getSystemResult(Long systemResultId) {
        return entityRetrievalUtils.getSystemResultOrThrow(systemResultId);
    }

    // ------------------------------------ Utility Methods ------------------------------------
    private <T, R> List<R> toIds(List<T> entities, Function<T, R> idMapper) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(idMapper)
                .collect(Collectors.toList());
    }

    private <T, R> List<R> toEntities(List<T> ids, Function<T, R> entityRetriever) {
        if (ids == null) {
            return Collections.emptyList();
        }
        return ids.stream()
                .map(entityRetriever)
                .collect(Collectors.toList());
    }
}
