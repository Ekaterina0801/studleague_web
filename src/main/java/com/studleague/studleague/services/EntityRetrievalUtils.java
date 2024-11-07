package com.studleague.studleague.services;

import com.studleague.studleague.entities.*;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.factory.TransferFactory;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.repository.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import org.springframework.stereotype.Component;


@Component
public class EntityRetrievalUtils {

    @Autowired
    private ControversialRepository controversialRepository;

    @Autowired
    private FlagRepository flagRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private TeamCompositionRepository teamCompositionRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TransferRepository transferRepository;

    private final Logger logger = LoggerFactory.getLogger(EntityRetrievalUtils.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemResultRepository systemResultRepository;

    private <T> T getEntityOrThrow(Optional<T> optionalEntity, String entityName, Long id) {
        return optionalEntity.orElseThrow(() -> {
            logger.warn("{} with id {} not found", entityName, id);
            return new RuntimeException(entityName + " not found with id: " + id);
        });
    }

    public <T> T getEntityByNameOrThrow(Optional<T> optionalEntity, String entityName, String name) {
        return optionalEntity.orElseThrow(() -> {
            logger.warn("{} with name {} not found", entityName, name);
            return new RuntimeException(entityName + " not found with name: " + name);
        });
    }

    public <T> T getEntityByTwoIdOrThrow(Optional<T> optionalEntity, String entityName, long id1, long id2) {
        return optionalEntity.orElseThrow(() -> {
            logger.warn("{} with id1 {} and id2 {} not found", entityName, id1, id2);
            return new RuntimeException(entityName + " not found with ids: " + id1 + " " + id2);
        });
    }

    public Controversial getControversialOrThrow(Long controversialId) {
        return getEntityOrThrow(controversialRepository.findById(controversialId), "Controversial", controversialId);
    }

    public Flag getFlagOrThrow(Long flagId) {
        return getEntityOrThrow(flagRepository.findById(flagId), "Flag", flagId);
    }

    public League getLeagueOrThrow(Long leagueId) {
        return getEntityOrThrow(leagueRepository.findById(leagueId), "League", leagueId);
    }

    public Player getPlayerOrThrow(Long playerId) {
        return getEntityOrThrow(playerRepository.findById(playerId), "Player", playerId);
    }

    public FullResult getResultOrThrow(Long resultId) {
        return getEntityOrThrow(resultRepository.findById(resultId), "FullResult", resultId);
    }

    public TeamComposition getTeamCompositionOrThrow(Long teamCompositionId) {
        return getEntityOrThrow(teamCompositionRepository.findById(teamCompositionId), "TeamComposition", teamCompositionId);
    }

    public Team getTeamOrThrow(Long teamId) {
        return getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
    }

    public Tournament getTournamentOrThrow(Long tournamentId) {
        return getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
    }

    public Transfer getTransferOrThrow(Long transferId) {
        return getEntityOrThrow(transferRepository.findById(transferId), "Transfer", transferId);
    }

    public User getUserOrThrow(Long userId)
    {
        return getEntityOrThrow(userRepository.findById(userId), "User", userId);
    }

    public SystemResult getSystemResultOrThrow(Long systemResultId)
    {
        return getEntityOrThrow(systemResultRepository.findById(systemResultId), "SystemResult", systemResultId);
    }


    public Controversial getControversialByResultIdAndQuestionNumberOrThrow(Long resultId, int questionNumber)
    {
        return getEntityByTwoIdOrThrow(controversialRepository.findByFullResultIdAndQuestionNumber(resultId, questionNumber), "Controversial", resultId, questionNumber);
    }

    public Flag getFlagByNameOrThrow(String name)
    {
        return getEntityByNameOrThrow(flagRepository.findByNameIgnoreCase(name), "Flag", name);
    }

    public League getLeagueByNameOrThrow(String name)
    {
        return getEntityByNameOrThrow(leagueRepository.findByNameIgnoreCase(name), "League", name);
    }

    public SystemResult getSystemResultByNameOrThrow(String name)
    {
        return getEntityByNameOrThrow(systemResultRepository.findByNameIgnoreCase(name), "SystemResult", name);
    }

    public Player getPlayerByIdSiteOrThrow(Long idSite)
    {
        return getEntityOrThrow(playerRepository.findByIdSite(idSite), "Player (by idSite)", idSite);
    }

    public FullResult getResultByTeamIdAndTournamentIdOrThrow(Long teamId, Long tournamentId)
    {
        return getEntityByTwoIdOrThrow(resultRepository.findByTeamIdAndTournamentId(teamId, tournamentId), "FullResult",teamId, tournamentId);
    }

    public TeamComposition getTeamCompositionByTournamentIdAndParentTeamIdOrThrow(Long tournamentId, Long teamId)
    {
        return getEntityByTwoIdOrThrow(teamCompositionRepository.findByTournamentIdAndParentTeamId(tournamentId,teamId),"TeamComposition", tournamentId,teamId);
    }

    public Team getTeamByIdSiteOrThrow(Long idSite)
    {
        return getEntityOrThrow(teamRepository.findByIdSite(idSite), "Team", idSite);
    }

    public Tournament getTournamentByIdSiteOrThrow(Long idSite)
    {
        return getEntityOrThrow(tournamentRepository.findByIdSite(idSite), "Tournament", idSite);
    }

    public User getUserByUsernameOrThrow(String username)
    {
        return getEntityByNameOrThrow(userRepository.findByUsername(username), "User", username);
    }
}


