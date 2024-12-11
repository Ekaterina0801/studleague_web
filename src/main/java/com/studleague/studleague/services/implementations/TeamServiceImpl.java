package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappers.PlayerMainInfoMapper;
import com.studleague.studleague.mappers.TeamMapper;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.specifications.TeamSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("teamService")
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private FlagRepository flagRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TeamCompositionRepository teamCompositionRepository;

    @Autowired
    private TeamCompositionService teamCompositionService;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private PlayerMainInfoMapper playerMainInfoMapper;

    @Autowired
    private TransferRepository transferRepository;


    @Override
    @Transactional(readOnly = true)
    public Team getTeamById(Long id) {
        return entityRetrievalUtils.getTeamOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    @Transactional
    public void saveTeam(Team team) {
        Long idSite = team.getIdSite();
        Long id = team.getId();
        Long leagueId = team.getLeague().getId();

        if (idSite != null) {
            if (teamRepository.existsByIdSite(idSite) && leagueContainsTeam(leagueId, idSite)) {
                Team existingTeam = entityRetrievalUtils.getTeamByIdSiteOrThrow(idSite);
                updateTeam(existingTeam, team);
                return;
            }
        }
        if (teamRepository.existsByTeamNameIgnoreCaseAndLeagueId(team.getTeamName(), leagueId)) {
            Team existingTeam = entityRetrievalUtils.getTeamByTeamNameIgnoreCaseAndLeagueIdOrThrow(team.getTeamName(), team.getLeague().getId());
            updateTeam(existingTeam, team);
            return;
        }
        if (id != null && teamRepository.existsById(id)) {
            Team existingTeam = entityRetrievalUtils.getTeamOrThrow(id);
            updateTeam(existingTeam, team);
            return;
        }

        teamRepository.save(team);
    }
    private boolean leagueContainsTeam(Long leagueId, Long idSite) {
        return teamRepository.existsByIdSiteAndLeagueId(idSite, leagueId);
    }


    @Transactional
    private void updateTeam(Team existingTeam, Team team) {
        existingTeam.setIdSite(team.getIdSite());
        existingTeam.setTeamCompositions(team.getTeamCompositions());
        existingTeam.setFlags(team.getFlags());
        existingTeam.setTournaments(team.getTournaments());
        existingTeam.setPlayers(team.getPlayers());
        existingTeam.setLeague(team.getLeague());
        existingTeam.setUniversity(team.getUniversity());
        existingTeam.setResults(team.getResults());
        teamRepository.save(existingTeam);
    }


    @Override
    @Transactional
    public void deleteTeam(Long id) {
        Team team = entityRetrievalUtils.getTeamOrThrow(id);

        if (!team.getTournaments().isEmpty()) {
            for (Tournament tournament : new ArrayList<>(team.getTournaments())) {
                tournament.deleteTeam(team);
            }
        }

        if (!team.getPlayers().isEmpty()) {
            List<Player> playersToRemove = new ArrayList<>(team.getPlayers());
            for (Player player : playersToRemove) {
                team.deletePlayerFromTeam(player);
            }
        }
        List<Transfer> transfers = transferRepository.findAllByTeamId(id);
        transferRepository.deleteAll(transfers);
        teamRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Team> teamsByLeague(Long leagueId) {
        return teamRepository.findAllByLeagueId(leagueId);
    }

    @Override
    @Transactional
    public Team addPlayerToTeam(Long teamId, Long playerId) {
        Player player = entityRetrievalUtils.getPlayerOrThrow(playerId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        team.addPlayerToTeam(player);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team deletePlayerFromTeam(Long teamId, Long playerId) {
        Player player = entityRetrievalUtils.getPlayerOrThrow(playerId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        team.deletePlayerFromTeam(player);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team addFlagToTeam(Long teamId, Long flagId) {
        Flag flag = entityRetrievalUtils.getFlagOrThrow(flagId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        team.addFlagToTeam(flag);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team addLeagueToTeam(Long teamId, Long leagueId) {
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        team.setLeague(league);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team deleteFlagFromTeam(Long teamId, Long flagId) {
        Flag flag = entityRetrievalUtils.getFlagOrThrow(flagId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        team.deleteFlagFromTeam(flag);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team getTeamByIdSite(Long idSite) {
        return entityRetrievalUtils.getTeamByIdSiteOrThrow(idSite);
    }

    @Override
    @Transactional
    public List<Team> getTeamsByPlayerId(Long playerId) {
        Player player = entityRetrievalUtils.getPlayerOrThrow(playerId);

        return player.getTeams();
    }

    @Override
    public boolean isManager(Long userId, Long teamId) {
        if (userId == null)
            return false;
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        return leagueService.isManager(userId, team.getLeague().getId());
    }

    @Override
    public boolean isManager(Long userId, TeamDTO teamDTO) {
        if (userId == null)
            return false;
        Team team = teamMapper.mapToEntity(teamDTO);
        return leagueService.isManager(userId, team.getLeague().getId());
    }

    @Override
    @Transactional
    public List<InfoTeamResults> getInfoTeamResultsByTeam(Long teamId) {
        List<InfoTeamResults> infoTeamResults = new ArrayList<>();
        Team team = getTeamById(teamId);
        List<FullResult> results = resultRepository.findAllByTeamId(teamId);

        HashMap<Tournament, FullResult> tournamentsResults = new HashMap<>();
        for (FullResult result : results) {
            tournamentsResults.put(result.getTournament(), result);
        }

        //HashMap<Tournament, List<Player>> tournamentsPlayers = getTournamentsPlayersByTeam(teamId);
        List<TeamComposition> teamCompositions = teamCompositionService.findByParentTeamId(teamId);
        int counter = 1;
        for (TeamComposition teamComposition : teamCompositions) {
            InfoTeamResults row = new InfoTeamResults();
            row.setId(counter++);
            row.setPlayers(teamComposition.getPlayers().stream().map(x -> playerMainInfoMapper.mapToDto(x)).collect(Collectors.toList()));
            row.setTeam(team);
            row.setTournament(teamComposition.getTournament());

            if (tournamentsResults.containsKey(teamComposition.getTournament())) {
                setScoreDetails(row, tournamentsResults.get(teamComposition.getTournament()));
            }

            infoTeamResults.add(row);
        }
        return infoTeamResults;
    }

    private void setScoreDetails(InfoTeamResults row, FullResult result) {
        String maskResults = result.getMask_results();
        List<Integer> answers = new ArrayList<>();
        List<Integer> questionNumbers = new ArrayList<>();
        int totalScore = 0;

        for (int i = 0; i < maskResults.length(); i++) {
            String answer = String.valueOf(maskResults.charAt(i));
            questionNumbers.add(i + 1);
            try {
                int number = Integer.parseInt(answer);
                answers.add(number);
                totalScore += number;
            } catch (NumberFormatException e) {
                answers.add(0);
            }
        }

        row.setAnswers(answers);
        row.setTotalScore(totalScore);
        row.setCountQuestions(maskResults.length());
        row.setQuestionNumbers(questionNumbers);
    }

    @Override
    @Transactional
    public boolean existsByIdSite(Long idSite) {
        return teamRepository.existsByIdSite(idSite);
    }

    @Override
    @Transactional
    public void deleteAllTeams() {
        teamRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getTeamsByFlagId(Long flagId) {
        Flag flag = entityRetrievalUtils.getFlagOrThrow(flagId);
        return flag.getTeams();
    }

    @Override
    public Page<Team> searchTeams(String name, Long leagueId, List<Long> flagIds, Sort sort, Pageable pageable) {
        Specification<Team> spec = TeamSpecification.searchTeams(name, leagueId, flagIds, sort);
        return teamRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getTeamsByPlayerIdAndLeagueId(Long playerId, Long leagueId) {
        return teamRepository.findAllByPlayerIdAndLeagueId(playerId, leagueId);
    }


}
