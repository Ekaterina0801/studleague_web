package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.*;
import com.studleague.studleague.dto.security.JwtAuthenticationResponse;
import com.studleague.studleague.dto.security.SignInRequest;
import com.studleague.studleague.dto.security.SignUpRequest;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.factory.*;
import com.studleague.studleague.repository.TeamCompositionRepository;
import com.studleague.studleague.services.LeagueResult;
import com.studleague.studleague.services.interfaces.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.expression.Numbers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class WebClientController {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://localhost:8080/api";

    private final ResultService resultService;
    private final ControversialService controversialService;
    private final TeamService teamService;

    @Autowired
    private TournamentFactory tournamentFactory;

    @Autowired
    private TeamFactory teamFactory;

    @Autowired
    private LeagueFactory leagueFactory;

    @Autowired
    private ControversialFactory controversialFactory;

    @Autowired
    private FullResultFactory fullResultFactory;

    @Autowired
    private PlayerFactory playerFactory;

    @Autowired
    private FlagFactory flagFactory;

    @Autowired
    private TransferFactory transferFactory;

    @Autowired
    private TeamCompositionRepository teamCompositionRepository;

    @Autowired
    private TeamCompositionService teamCompositionService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private SystemResultService systemResultService;

    @Autowired
    public WebClientController(ResultService resultService, TournamentService tournamentService,
                               ControversialService controversialService, PlayerService playerService,
                               TeamService teamService) {
        this.resultService = resultService;
        this.controversialService = controversialService;
        this.teamService = teamService;
    }

    public static List<Integer> generateNumbers(int n) {
        return IntStream.rangeClosed(1, n).boxed().collect(Collectors.toList());
    }

    private List<LeagueDTO> fetchLeagues() {
        return fetchFromApi(BASE_URL + "/leagues", new ParameterizedTypeReference<List<LeagueDTO>>() {
        });
    }

    private HttpEntity<?> createHttpEntityWithToken() {
                String token = (String) httpSession.getAttribute("token");

                HttpHeaders headers = new HttpHeaders();
                if (token != null) {
                        headers.set("Authorization", "Bearer " + token);
                    }

                return new HttpEntity<>(headers);
            }
    private <T> T fetchFromApi(String url, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, createHttpEntityWithToken(), responseType);
            return responseEntity.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value="/leagues/{league_id}/results")
    public String leagueResultsView(@PathVariable long league_id, Model model) {
        League league = leagueService.getLeagueWithResults(league_id);
        List<LeagueDTO> leagueDTOs = fetchLeagues();
        List<League> leagues = leagueDTOs != null
                ? leagueDTOs.stream().map(leagueFactory::toEntity).toList()
                : null;
        List<SystemResult> availableSystems = systemResultService.findAll();
        model.addAttribute("league", league);
        List<LeagueResult> standings = resultService.calculateResultsBySystem(league_id, league.getSystemResult().getName(), league.getSystemResult().getCountNotIncludedGames());
        model.addAttribute("standings", standings);
        model.addAttribute("leagues", leagues);
        var countGames = IntStream.rangeClosed(1, league.getTournaments().toArray().length).toArray();
        model.addAttribute("countGames",countGames);
        model.addAttribute("availableSystems", availableSystems);
        return "league-results";
    }

    @RequestMapping("/leagues/{league_id}/tournaments/{tournament_id}/controversials")
    public String controversialsTournamentView(@PathVariable long league_id, @PathVariable long tournament_id, Model model) {
        TournamentDTO tournamentDTO = fetchFromApi(BASE_URL + "/tournaments/" + tournament_id, new ParameterizedTypeReference<TournamentDTO>() {
        });
        Tournament tournament = tournamentDTO != null ? tournamentFactory.toEntity(tournamentDTO) : null;

        List<LeagueDTO> leagueDTOs = fetchLeagues();
        List<League> leagues = leagueDTOs != null
                ? leagueDTOs.stream().map(leagueFactory::toEntity).toList()
                : null;

        List<Controversial> controversials = controversialService.getControversialsByTournamentId(tournament_id);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("leagues", leagues);
        model.addAttribute("controversials", controversials);
        model.addAttribute("numbers", generateNumbers(controversials != null ? controversials.size() : 0));
        model.addAttribute("tournament", tournament);
        return "controversials";
    }

    @RequestMapping("/leagues/{league_id}/tournaments/{tournament_id}/results")
    public String tournamentResultsView(@PathVariable long league_id, @PathVariable long tournament_id, Model model) {
        List<FullResultDTO> resultsDTO = fetchFromApi(BASE_URL + "/tournaments/" + tournament_id + "/results", new ParameterizedTypeReference<List<FullResultDTO>>() {
        });
        List<FullResult> results = resultsDTO != null
                ? resultsDTO.stream().map(fullResultFactory::toEntity).toList()
                : null;

        TournamentDTO tournamentDTO = fetchFromApi(BASE_URL + "/tournaments/" + tournament_id, new ParameterizedTypeReference<TournamentDTO>() {
        });
        Tournament tournament = tournamentDTO != null ? tournamentFactory.toEntity(tournamentDTO) : null;

        List<LeagueDTO> leagueDTOs = fetchLeagues();
        List<League> leagues = leagueDTOs != null
                ? leagueDTOs.stream().map(leagueFactory::toEntity).toList()
                : null;

        List<TeamComposition> teamCompositions = teamCompositionService.findByTournamentId(tournament_id);
        model.addAttribute("tournament", tournament);
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("tableResult", resultService.fullResultsToTable(results));
        model.addAttribute("teamCompositions", teamCompositions);
        return "results-tournament";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}/results")
    public String teamResultsView(@PathVariable long league_id, @PathVariable long team_id, Model model) {
        model.addAttribute("leagues", fetchLeagues());
        model.addAttribute("leagueId", league_id);
        return "leagueResults";
    }

    @RequestMapping("/leagues/{league_id}/teams")
    public String teamsView(@PathVariable long league_id, Model model) {
        List<TeamDTO> teamDTOs = fetchFromApi(BASE_URL + "/leagues/" + league_id + "/teams", new ParameterizedTypeReference<List<TeamDTO>>() {
        });
        model.addAttribute("teams", teamDTOs != null ? teamDTOs.stream().map(teamFactory::toEntity).toList() : null);
        model.addAttribute("leagues", fetchLeagues());
        model.addAttribute("leagueId", league_id);
        return "teams";
    }

    @RequestMapping(value = "/leagues/{league_id}/tournaments")
    public String tournamentsView(@PathVariable long league_id, Model model) {
        List<TournamentDTO> tournamentDTOs = fetchFromApi(BASE_URL + "/leagues/" + league_id + "/tournaments", new ParameterizedTypeReference<>() {
        });
        model.addAttribute("tournaments", tournamentDTOs != null ? tournamentDTOs.stream().map(tournamentFactory::toEntity).toList() : null);
        model.addAttribute("leagues", fetchLeagues());
        model.addAttribute("leagueId", league_id);
        return "tournaments";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}")
    public String teamProfileView(@PathVariable long league_id, @PathVariable long team_id, Model model) {
        model.addAttribute("leagues", fetchLeagues());

        TeamDTO teamDTO = fetchFromApi(BASE_URL + "/teams/" + team_id, new ParameterizedTypeReference<TeamDTO>() {
        });
        Team team = teamDTO != null ? teamFactory.toEntity(teamDTO) : null;

        List<Flag> allFlags = fetchFromApi(BASE_URL + "/flags", new ParameterizedTypeReference<List<FlagDTO>>() {
        }).stream().map(x -> flagFactory.toEntity(x)).toList();
        List<Transfer> transfers = fetchFromApi(BASE_URL + "/teams/" + team_id + "/transfers", new ParameterizedTypeReference<List<TransferDTO>>() {
        }).stream().map(x -> transferFactory.toEntity(x)).toList();
        List<InfoTeamResults> resultsTable = teamService.getInfoTeamResultsByTeam(team_id);
        List<Integer> numbers = generateNumbers(team != null ? team.getTournaments().size() : 0);
        List<TeamComposition> teamCompositions = teamCompositionService.findByParentTeamId(team_id);
        //HashMap<Tournament, List<Player>> tournamentsPlayers = team != null ? teamService.getTournamentsPlayersByTeam(team.getId()) : new HashMap<>();

        model.addAttribute("leagueId", league_id);
        model.addAttribute("transfers", transfers);
        model.addAttribute("results", resultsTable);
        model.addAttribute("numbers", numbers);
        model.addAttribute("allFlags", allFlags);
        model.addAttribute("team", team);
        model.addAttribute("teamCompositions", teamCompositions);
        return "team-profile";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}/players/{player_id}")
    public String playerProfileView(@PathVariable long league_id, @PathVariable long team_id, @PathVariable long player_id, Model model) {
        model.addAttribute("leagues", fetchLeagues());

        PlayerDTO playerDTO = fetchFromApi(BASE_URL + "/players/" + player_id, new ParameterizedTypeReference<PlayerDTO>() {
        });
        Player player = playerDTO != null ? playerFactory.toEntity(playerDTO) : null;

        List<TeamDTO> allTeamsDTO = fetchFromApi(BASE_URL + "/teams", new ParameterizedTypeReference<List<TeamDTO>>() {
        });
        List<Team> allTeams = new ArrayList<>(allTeamsDTO != null ? allTeamsDTO.stream().map(teamFactory::toEntity).toList() : null);

        List<Team> currentTeams = player != null ? player.getTeams() : null;
        if (currentTeams != null) {
            allTeams.removeAll(currentTeams);
        }

        model.addAttribute("oldTeams", currentTeams);
        model.addAttribute("newTeams", allTeams);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("player", player);

        return "player";
    }

    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("signUpRequest", model.containsAttribute("signUpRequest") ?
                model.getAttribute("signUpRequest") : new SignUpRequest());
        return "registration";
    }




    @PostMapping("/sign-up")
    public String processSignUp(@Valid @ModelAttribute SignUpRequest signUpRequest,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirm())) {
            bindingResult.rejectValue("confirm", "error.confirm", "Пароли не совпадают");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("signUpRequest", signUpRequest);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.signUpRequest", bindingResult);
            return "redirect:/sign-up";
        }

        try {
            ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity(
                    "http://localhost:8080/auth/sign-up",
                    signUpRequest,
                    JwtAuthenticationResponse.class
            );

            redirectAttributes.addFlashAttribute("message", "Регистрация успешна! Пожалуйста, войдите в систему.");
            return "redirect:/sign-in";
        } catch (HttpClientErrorException e) {
            System.err.println("Ошибка клиента: " + e.getStatusCode());
            System.err.println("Ответ от сервера: " + e.getResponseBodyAsString());
            redirectAttributes.addFlashAttribute("error", "Ошибка регистрации: " + e.getResponseBodyAsString());
            return "redirect:/sign-up";
        } catch (HttpServerErrorException e) {
            System.err.println("Ошибка сервера: " + e.getStatusCode());
            System.err.println("Ответ от сервера: " + e.getResponseBodyAsString());
            return "redirect:/sign-up";
        }

    }



    @GetMapping("/sign-in")
    public String showSignInForm(Model model) {
        model.addAttribute("signInRequest", new SignInRequest());
        return "login";
    }

    @PostMapping("/sign-in")
    public String processSignIn(@ModelAttribute SignInRequest signInRequest, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SignInRequest> entity = new HttpEntity<>(signInRequest, headers);

            ResponseEntity<JwtAuthenticationResponse> response = restTemplate.exchange(
                    "http://localhost:8080/auth/sign-in",
                    HttpMethod.POST,
                    entity,
                    JwtAuthenticationResponse.class
            );

            // Сохраняем токен в сессии
            //String token = response.getBody().getToken();
            //session.setAttribute("token", token);

            redirectAttributes.addFlashAttribute("message", "Успешная авторизация!");
            return "redirect:/leagues/1/tournaments";
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка авторизации: " + e.getResponseBodyAsString());
            return "redirect:/auth/sign-in";
        }
    }



    @RequestMapping("/players")
    public String playersView() {
        return "players-view";
    }

    @GetMapping("/auth-status")
    public ResponseEntity<String> authStatus(HttpServletRequest request) {
        // Извлекаем аутентификацию из SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, что пользователь аутентифицирован
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            // Получаем токен из заголовка Authorization
            String authHeader = request.getHeader("Authorization");
            String token = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // Извлекаем сам токен, убирая префикс "Bearer "
            }

            return ResponseEntity.ok("Authenticated user: " + auth.getName() + ", Token: " + token);
        }

        // Если пользователь не аутентифицирован, возвращаем ошибку
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }

}
