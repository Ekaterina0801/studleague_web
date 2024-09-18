package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tournaments")
@JsonIdentityInfo(scope= Tournament.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Tournament {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "id_site")
    @JsonProperty("idSite")
    private String idSite;

    @Column(name = "date_of_start")
    @JsonProperty("dateOfStart")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateOfStart;

    @Column(name = "date_of_final")
    @JsonProperty("dateOfFinal")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateOfEnd;

    @ManyToMany(mappedBy = "tournaments")
    //@JsonBackReference(value = "tournaments")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<League> leagues = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<FullResult> results = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "tournaments_teams",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    //@JsonManagedReference
    //@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<Team> teams = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "tournaments_players",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    //@JsonManagedReference
    //@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<Player> players = new ArrayList<>();


    public Tournament() {
    }

    public Tournament(int id, String name, String id_site, Date date_of_start, Date date_of_end) {
        this.id = id;
        this.name = name;
        this.idSite = id_site;
        this.dateOfStart = date_of_start;
        this.dateOfEnd = date_of_end;
    }

    /*public void addResultToResults(Team team, int result )
    {
        if (results==null)
        {
            results = new HashMap<>();
        }
        results.put(team,result);
    }*/

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdSite() {
        return idSite;
    }

    public void setIdSite(String idSite) {
        this.idSite = idSite;
    }

    public Date getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(Date dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public Date getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(Date dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

    public List<FullResult> getResults() {
        return results;
    }

    public void setResults(List<FullResult> results) {
        this.results = results;
    }

    public void addResult(FullResult fullResult) {
        if (results == null) {
            results = new ArrayList<>();
        } else {
            results.add(fullResult);
        }
    }

    public void deleteResult(FullResult fullResult) {
        if (results != null) {
            results.remove(fullResult);
            //fullResult.getTournament()..remove(this);
        }
    }

    public void addPlayer(Player player) {
        if (players == null) {
            players = new ArrayList<>();
        } else {
            players.add(player);
            player.addTournamentToPlayer(this);
        }
    }

    public void deletePlayer(Player player) {
        if (players != null) {
            players.remove(player);
            //fullResult.getTournament()..remove(this);
        }
    }

    public void addTeam(Team team) {
        if (teams == null) {
            teams = new ArrayList<>();
        } else {
            teams.add(team);
            team.addTournamentToTeam(this);
        }
    }

    public void deleteTeam(Team team) {
        if (teams != null) {
            teams.remove(team);
            //fullResult.getTournament()..remove(this);
        }
    }


}
