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


    // ------------------------------------ Tournament Mapping ------------------------------------
    @Named("tournamentIdToTournament")
    public Tournament mapTournamentIdToTournament(Long id) {
        return entityRetrievalUtils.getTournamentOrThrow(id);
    }

    @Named("tournamentIdsToTournaments")
    public List<Tournament> mapTournamentIdsToTournaments(List<Long> tournamentIds) {
        return toEntities(tournamentIds, entityRetrievalUtils::getTournamentOrThrow);
    }

    @Named("tournamentsToTournamentIds")
    public List<Long> mapTournamentsToTournamentIds(List<Tournament> tournaments) {
        return toIds(tournaments, Tournament::getId);
    }

    // ------------------------------------ Team Mapping ------------------------------------
    @Named("teamIdToTeam")
    public Team mapTeamIdToTeam(Long teamId) {
        return entityRetrievalUtils.getTeamOrThrow(teamId);
    }

    @Named("teamIdsToTeams")
    public List<Team> mapTeamIdsToTeams(List<Long> teamIds) {
        return toEntities(teamIds, entityRetrievalUtils::getTeamOrThrow);
    }

    @Named("teamsToTeamIds")
    public List<Long> mapTeamsToTeamIds(List<Team> teams) {
        return toIds(teams, Team::getId);
    }

    // ------------------------------------ League Mapping ------------------------------------
    @Named("leagueIdToLeague")
    public League leagueIdToLeague(Long leagueId) {
        return entityRetrievalUtils.getLeagueOrThrow(leagueId);
    }


    @Named("leaguesIdsToEntities")
    public List<League> mapLeaguesIdsToEntities(List<Long> leagueIds) {
        return toEntities(leagueIds, entityRetrievalUtils::getLeagueOrThrow);
    }

    @Named("leaguesToIds")
    public List<Long> mapLeaguesToIds(List<League> leagues) {
        return toIds(leagues, League::getId);
    }

    // ------------------------------------ User Mapping ------------------------------------
    @Named("userIdToUser")
    public User mapUserIdToUser(Long userId) {
        return entityRetrievalUtils.getUserOrThrow(userId);
    }

    @Named("userIdsToManagers")
    public List<User> mapUserIdsToManagers(List<Long> userIds) {
        return toEntities(userIds, entityRetrievalUtils::getUserOrThrow);
    }

    @Named("managersToUserIds")
    public List<Long> mapManagersToUserIds(List<User> managers) {
        return toIds(managers, User::getId);
    }

    // ------------------------------------ Player Mapping ------------------------------------
    @Named("playerIdToPlayer")
    public Player mapPlayerIdToPlayer(Long playerId) {
        return entityRetrievalUtils.getPlayerOrThrow(playerId);
    }

    // ------------------------------------ Result Mapping ------------------------------------
//    @Named("toEntityResultIfExists")
//    FullResult toEntityIfExists(ResultMainInfoDTO dto) {
//        return dto != null ? resultMainInfoMapper.mapToEntity(dto) : null;
//    }

    @Named("systemResultIdToSystemResult")
    public SystemResult getSystemResult(Long systemResultId) {
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
