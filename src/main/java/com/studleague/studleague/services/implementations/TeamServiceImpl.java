package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.factory.TeamFactory;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import com.studleague.studleague.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private TeamFactory teamFactory;


    @Override
    @Transactional
    public Team getTeamById(Long id) {
        return entityRetrievalUtils.getTeamOrThrow(id);
    }

    @Override
    @Transactional
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    @Transactional
    public void saveTeam(Team team) {

        Long idSite = team.getIdSite();
        Long id = team.getId();
        if (id != null) {
            if (teamRepository.existsById(id)) {
                Team existingTeam = entityRetrievalUtils.getTeamOrThrow(id);
                updateTeam(existingTeam, team);
            }
        } else if (idSite != 0) {
            if (teamRepository.existsByIdSite(idSite)) {
                Team existingTeam = entityRetrievalUtils.getTeamByIdSiteOrThrow(idSite);
                updateTeam(existingTeam, team);
            } else {
                teamRepository.save(team);
            }
        } else {
            teamRepository.save(team);
        }
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
        teamRepository.deleteById(id);
    }

    @Override
    @Transactional
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
        Team team = teamFactory.toEntity(teamDTO);
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
            row.setNumber(counter++);
            row.setPlayers(teamComposition.getPlayers());
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
    public List<Team> getTeamsByFlagId(Long flagId) {
        Flag flag = entityRetrievalUtils.getFlagOrThrow(flagId);
        return flag.getTeams();
    }

    /*@Override
    @Transactional
    public List<InfoTeamResults> getInfoTeamResultsByTeam(int team_id)
    {
        List<InfoTeamResults> infoTeamResults = new ArrayList<>();
        InfoTeamResults row = new InfoTeamResults();
        Team team = getTeamById(team_id);
        List<FullResult> results = fullResultDao.getResultsForTeam(team_id);
        HashMap<Tournament, FullResult> tournamentsResults = new HashMap<>();
        for (FullResult result: results)
        {
            tournamentsResults.put(result.getTournament(), result);
        }
        int counter = 1;
        HashMap<Tournament, List<Player>> tournamentsPlayers = getTournamentsPlayersByTeam(team_id);
        for (Tournament tournament:tournamentsPlayers.keySet())
        {
            row.setNumber(counter);
            counter+=1;
            row.setPlayers(tournamentsPlayers.get(tournament));
            row.setTeam(team);
            row.setTournament(tournament);
            if (tournamentsResults.containsKey(tournament))
            {
                FullResult result = tournamentsResults.get(tournament);
                String maskResults = result.getMask_results();
                List<Integer> answers = new ArrayList<>();
                List<Integer> questionNumbers = new ArrayList<>();
                int totalScore = 0;
                for (int i = 0; i < maskResults.length(); i++){
                    String answer = String.valueOf(maskResults.charAt(i));
                    questionNumbers.add(i+1);
                    try {
                        int number = Integer.parseInt(answer);
                        answers.add(number);
                        totalScore+=number;
                    } catch (NumberFormatException e) {
                        answers.add(0);
                    }
                }
                row.setAnswers(answers);
                row.setTotalScore(totalScore);
                row.setCountQuestions(maskResults.length());
                row.setQuestionNumbers(questionNumbers);

            }
            infoTeamResults.add(row);
        }
        return infoTeamResults;
    }
*/


    @Override
    @Transactional
    public List<Team> getTeamsByPlayerIdAndLeagueId(Long playerId, Long leagueId) {
        return teamRepository.findAllByPlayerIdAndLeagueId(playerId, leagueId);
    }


}
