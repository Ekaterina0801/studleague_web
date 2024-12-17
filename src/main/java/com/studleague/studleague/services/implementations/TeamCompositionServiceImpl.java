package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.teamComposition.TeamCompositionDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.mappers.teamComposition.TeamCompositionMapper;
import com.studleague.studleague.repository.TeamCompositionRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.PlayerService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.specifications.TeamCompositionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("teamCompositionService")
public class TeamCompositionServiceImpl implements TeamCompositionService {

    @Autowired
    private TeamCompositionRepository teamCompositionRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private TeamCompositionMapper teamCompositionMapper;

    @Autowired
    @Lazy
    private TeamService teamService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Override
    @Transactional(readOnly = true)
    public TeamComposition findById(Long id) {
        return entityRetrievalUtils.getTeamCompositionOrThrow(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void save(TeamComposition teamComposition) {
        Long tournamentId = teamComposition.getTournament().getId();
        Long parentTeamId = teamComposition.getParentTeam().getId();
        Long id = teamComposition.getId();

        Optional<TeamComposition> existingOpt =
                id != null ? teamCompositionRepository.findById(id)
                        : teamCompositionRepository.findByTournamentIdAndParentTeamId(tournamentId, parentTeamId);

        if (existingOpt.isPresent()) {
            TeamComposition existingTeamComposition = existingOpt.get();
            update(existingTeamComposition, teamComposition);
            teamCompositionRepository.saveAndFlush(existingTeamComposition);
        } else {
            if (teamCompositionRepository.existsByTournamentIdAndParentTeamId(tournamentId, parentTeamId)) {
                return;
            }
            teamCompositionRepository.save(teamComposition);
        }
    }

    @Override
    public boolean existsByTeamAndTournament(Long tournamentId, Long teamId) {
        return teamCompositionRepository.existsByTournamentIdAndParentTeamId(tournamentId, teamId);
    }


    private void update(TeamComposition existingTeamComposition, TeamComposition teamComposition)
    {
        existingTeamComposition.setParentTeam(teamComposition.getParentTeam());
        existingTeamComposition.setTournament(teamComposition.getTournament());
        existingTeamComposition.setPlayers(teamComposition.getPlayers());
    }

    @Transactional(readOnly = true)
    @Override
    public List<TeamComposition> findByTournamentId(Long tournamentId) {
        return teamCompositionRepository.findAllByTournamentId(tournamentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TeamComposition> findByParentTeamId(Long parentTeamId) {
        return teamCompositionRepository.findAllByParentTeamId(parentTeamId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TeamComposition> findAll() {
        return teamCompositionRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteAll() {
        teamCompositionRepository.deleteAll();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        teamCompositionRepository.deleteById(id);
    }

    @Override
    public boolean isManager(Long userId, Long teamCompositionId) {
        if (userId==null)
            return false;
        TeamComposition teamComposition = entityRetrievalUtils.getTeamCompositionOrThrow(teamCompositionId);
        Long leagueId = teamComposition.getParentTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    public boolean isManager(Long userId, TeamCompositionDTO teamCompositionDTO) {
        if (userId==null)
            return false;
        TeamComposition teamComposition = teamCompositionMapper.mapToEntity(teamCompositionDTO);
        Long leagueId = teamComposition.getParentTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    public List<TeamComposition> searchTeamCompositions(Long teamId, Long tournamentId, Sort sort) {
        Specification<TeamComposition> spec = TeamCompositionSpecification.searchTeamCompositions(teamId, tournamentId, sort);
        return teamCompositionRepository.findAll(spec);
    }

    @Override
    public List<TeamCompositionDTO> processTeamCompositions(List<Map<String, Object>> data, Long leagueId, Long tournamentId) {
        List<TeamCompositionDTO> compositions = new ArrayList<>();
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);

        Map<String, List<Player>> teamPlayersMap = new HashMap<>();

        String teamNameKey = findTeamNameKey(data);

        if (teamNameKey == null) {
            throw new IllegalArgumentException("Не найден столбец с названием команды.");
        }

        for (Map<String, Object> row : data) {
            String teamName = (String) row.get(teamNameKey);

            String surname = (String) row.get("Фамилия");
            String name = (String) row.get("Имя");
            String patronymic = (String) row.get("Отчество");
            String playerFullName = (String) row.get("ФИО игрока");

            if (teamName == null || teamName.isEmpty()) {
                continue;
            }

            if ((surname == null || surname.isEmpty()) && (playerFullName == null || playerFullName.isEmpty())) {
                continue;
            }

            if (playerFullName != null && !playerFullName.isEmpty()) {
                String[] nameParts = playerFullName.trim().split("\\s+");
                surname = nameParts.length > 0 ? nameParts[0] : "";
                name = nameParts.length > 1 ? nameParts[1] : "";
                patronymic = nameParts.length > 2 ? nameParts[2] : "";
            }

            if (name == null || name.isEmpty() || surname == null || surname.isEmpty()) {
                continue;
            }

            Player player = resolveOrCreatePlayer(name.trim(), patronymic != null ? patronymic.trim() : "", surname.trim());
            teamPlayersMap.computeIfAbsent(teamName, k -> new ArrayList<>()).add(player);
        }

        for (Map.Entry<String, List<Player>> entry : teamPlayersMap.entrySet()) {
            String teamName = entry.getKey();
            List<Player> players = entry.getValue();

            Team team = resolveOrCreateTeam(teamName, leagueId, tournament);

            TeamComposition teamComposition = processTeamWithPlayers(team, tournament, players);
            for (Player player : teamComposition.getPlayers()) {
                team.addPlayerToTeam(player);
            }
            teamCompositionRepository.save(teamComposition);
            teamService.saveTeam(team);
            compositions.add(teamCompositionMapper.mapToDto(teamComposition));
        }

        return compositions;
    }

    /**
     * Ищет ключ, содержащий информацию о названии команды.
     */
    private String findTeamNameKey(List<Map<String, Object>> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        Map<String, Object> firstRow = data.get(0);
        for (String key : firstRow.keySet()) {
            if (key.toLowerCase().contains("название")) {
                return key;
            }
        }

        return null;
    }

    private Player resolveOrCreatePlayer(String name, String patronymic, String surname) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Player> playerPage = playerService.searchPlayers(name, patronymic, surname, null, null, null, null, pageable);

        if (playerPage.isEmpty()) {
            Player player = Player.builder()
                    .name(name)
                    .patronymic(patronymic)
                    .surname(surname)
                    .build();
            playerService.savePlayer(player);
            return player;
        } else {
            return playerPage.getContent().get(0);
        }
    }

    private TeamComposition processTeamWithPlayers(Team team, Tournament tournament, List<Player> players) {
        TeamComposition teamComposition = teamCompositionRepository
                .findByTournamentIdAndParentTeamId(tournament.getId(), team.getId())
                .orElseGet(() -> TeamComposition.builder()
                        .parentTeam(team)
                        .tournament(tournament)
                        .players(new ArrayList<>())
                        .build());

        teamComposition.getPlayers().clear();
        teamComposition.getPlayers().addAll(players);

        return teamComposition;
    }

    private Team resolveOrCreateTeam(String teamName, Long leagueId, Tournament tournament) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Team> teamPage = teamService.searchTeams(teamName, leagueId, null, null, pageable);
        Team team;
        if (teamPage.getContent().isEmpty()) {
            team = createNewTeam(teamName, leagueId, tournament);
        } else {
            team = teamPage.getContent().get(0);
        }

        linkTeamToTournament(team, tournament);
        return team;
    }

    private Team createNewTeam(String teamName, Long leagueId, Tournament tournament) {
        Team team = Team.builder()
                .teamName(teamName)
                .tournaments(new ArrayList<>(List.of(tournament)))
                .league(entityRetrievalUtils.getLeagueOrThrow(leagueId))
                .build();
        teamService.saveTeam(team);
        return team;
    }

    private void linkTeamToTournament(Team team, Tournament tournament) {
        if (teamCompositionRepository.existsByTournamentIdAndParentTeamId(tournament.getId(), team.getId())) {
            return;
        }
        TeamComposition teamComposition = TeamComposition.builder()
                .parentTeam(team)
                .tournament(tournament)
                .build();
        team.getTeamCompositions().add(teamComposition);
        tournament.addTeamComposition(teamComposition);
        tournament.addTeam(team);
        tournamentRepository.save(tournament);
    }








}
