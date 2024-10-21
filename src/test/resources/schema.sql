

CREATE TABLE leagues(
	id bigint NOT NULL AUTO_INCREMENT,
	name varchar(60),
    PRIMARY KEY(id)
);
CREATE TABLE teams (
  id bigint NOT NULL AUTO_INCREMENT,
  team_name varchar(40),
  university varchar(40),
  league_id bigint,
  id_site bigint,
  PRIMARY KEY(id),
  FOREIGN KEY(league_id) REFERENCES leagues(id)
);
CREATE TABLE players (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(30),
  patronymic varchar(30),
  surname varchar(30),
  university varchar(40),
  date_of_birth date,
  id_site bigint,
  PRIMARY KEY (id)
);

CREATE TABLE teams_players(
	id bigint NOT NULL AUTO_INCREMENT,
    team_id bigint,
    player_id bigint,
    PRIMARY KEY(id),
    FOREIGN KEY(team_id) REFERENCES teams(id),
    FOREIGN KEY(player_id) REFERENCES players(id)
);
CREATE TABLE flags(
	id bigint NOT NULL AUTO_INCREMENT,
    name varchar(40),
    PRIMARY KEY(id)
);
CREATE TABLE teams_flags(
	id bigint NOT NULL AUTO_INCREMENT,
	team_id bigint NOT NULL,
    flag_id bigint NOT NULL,
    PRIMARY KEY (id),
	FOREIGN KEY(team_id) REFERENCES teams(id),
    FOREIGN KEY(flag_id) REFERENCES flags(id)
);

CREATE TABLE tournaments(
	id bigint NOT NULL AUTO_INCREMENT,
    name varchar(100),
    id_site bigint,
    date_of_start date,
    date_of_final date,
    PRIMARY KEY(id)
);

CREATE TABLE tournaments_teams(
	id bigint NOT NULL AUTO_INCREMENT,
	tournament_id bigint,
    team_id bigint,
    PRIMARY KEY(id),
    FOREIGN KEY(tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY(team_id) REFERENCES teams(id)
);

CREATE TABLE tournaments_players(
	id bigint NOT NULL AUTO_INCREMENT,
	tournament_id bigint,
    player_id bigint,
    PRIMARY KEY(id),
    FOREIGN KEY(tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY(player_id) REFERENCES players(id)
);


CREATE TABLE transfers(
	id bigint NOT NULL AUTO_INCREMENT,
    transfer_date date,
    player_id bigint,
    old_team_id bigint,
    new_team_id bigint,
    comments varchar(40),
    PRIMARY KEY(id),
    FOREIGN KEY(player_id) REFERENCES players(id),
    FOREIGN KEY(old_team_id) REFERENCES teams(id),
    FOREIGN KEY(new_team_id) REFERENCES teams(id)
);


CREATE TABLE fullResults(
	id bigint NOT NULL AUTO_INCREMENT,
    team_id bigint,
    tournament_id bigint,
    question_number bigint,
    result bigint,
    contraversial varchar(40),
    PRIMARY KEY(id),
    FOREIGN KEY(team_id) REFERENCES teams(id),
    FOREIGN KEY(tournament_id) REFERENCES tournaments(id)
);



CREATE TABLE leagues_tournaments(
	id bigint NOT NULL AUTO_INCREMENT,
	league_id bigint,
    tournament_id bigint,
    PRIMARY KEY(id),
    FOREIGN KEY(league_id) REFERENCES leagues(id),
    FOREIGN KEY(tournament_id) REFERENCES tournaments(id)
);