package com.studleague.studleague.entities;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
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
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="^(\\d{2}/\\d{2}/\\d{4})|^(\\d{4}-\\d{2}-\\d{2})")
    private Date dateOfStart;

    @Column(name = "date_of_final")
    @JsonProperty("dateOfEnd")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="^(\\d{2}/\\d{2}/\\d{4})|^(\\d{4}-\\d{2}-\\d{2})")
    private Date dateOfEnd;



    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(name="leagues_tournaments",
            joinColumns = @JoinColumn(name="tournament_id"), inverseJoinColumns=@JoinColumn(name="league_id"))
    private List<League> leagues = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<FullResult> results = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "tournaments_teams",
            joinColumns = @JoinColumn(name = "tournament_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id")
    )

    //@JsonManagedReference
    //@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<Team> teams = new ArrayList<>();

    @ManyToMany(targetEntity = Player.class, cascade = {CascadeType.DETACH,CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "tournaments_players",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    //@JsonManagedReference
    //@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<Player> players = new ArrayList<>();


    public Tournament(int id, String name, String id_site, Date date_of_start, Date date_of_end) {
        this.id = id;
        this.name = name;
        this.idSite = id_site;
        this.dateOfStart = date_of_start;
        this.dateOfEnd = date_of_end;
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

    public void addLeague(League league){
        if(leagues==null){
            leagues = new ArrayList<>();
        }
        if (!leagues.contains(league))
        {
            leagues.add(league);
            league.getTournaments().add(this);
            //league.addTournamentToLeague(this);
        }
    }


}
