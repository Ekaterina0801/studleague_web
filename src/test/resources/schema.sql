
CREATE TABLE IF NOT EXISTS controversials (
    id BIGINT PRIMARY KEY,
    answer VARCHAR(255),
    appeal_jury_comment VARCHAR(255),
    comment VARCHAR(255),
    issued_at DATETIME(6),
    question_number INT,
    resolved_at VARCHAR(255),
    status VARCHAR(255),
    result_id BIGINT
);
CREATE TABLE IF NOT EXISTS leagues(
	id bigint NOT NULL AUTO_INCREMENT,
	name varchar(60),
    PRIMARY KEY(id)
);
CREATE TABLE IF NOT EXISTS teams (
  id bigint NOT NULL AUTO_INCREMENT,
  team_name varchar(40),
  university varchar(40),
  league_id bigint,
  id_site bigint,
  PRIMARY KEY(id),
  FOREIGN KEY(league_id) REFERENCES leagues(id)
);
CREATE TABLE IF NOT EXISTS players (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(30),
  patronymic varchar(30),
  surname varchar(30),
  university varchar(40),
  date_of_birth date,
  id_site bigint,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS teams_players(
	id bigint NOT NULL AUTO_INCREMENT,
    team_id bigint,
    player_id bigint,
    PRIMARY KEY(id),
    FOREIGN KEY(team_id) REFERENCES teams(id),
    FOREIGN KEY(player_id) REFERENCES players(id)
);
CREATE TABLE IF NOT EXISTS flags(
	id bigint NOT NULL AUTO_INCREMENT,
    name varchar(40),
    PRIMARY KEY(id)
);
CREATE TABLE IF NOT EXISTS teams_flags(
	id bigint NOT NULL AUTO_INCREMENT,
	team_id bigint NOT NULL,
    flag_id bigint NOT NULL,
    PRIMARY KEY (id),
	FOREIGN KEY(team_id) REFERENCES teams(id),
    FOREIGN KEY(flag_id) REFERENCES flags(id)
);

CREATE TABLE IF NOT EXISTS tournaments(
	id bigint NOT NULL AUTO_INCREMENT,
    name varchar(100),
    id_site bigint,
    date_of_start date,
    date_of_final date,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS tournaments_teams(
	id bigint NOT NULL AUTO_INCREMENT,
	tournament_id bigint,
    team_id bigint,
    PRIMARY KEY(id),
    FOREIGN KEY(tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY(team_id) REFERENCES teams(id)
);

CREATE TABLE IF NOT EXISTS tournaments_players(
	id bigint NOT NULL AUTO_INCREMENT,
	tournament_id bigint,
    player_id bigint,
    PRIMARY KEY(id),
    FOREIGN KEY(tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY(player_id) REFERENCES players(id)
);


CREATE TABLE IF NOT EXISTS transfers(
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


CREATE TABLE IF NOT EXISTS full_results(
	id bigint NOT NULL AUTO_INCREMENT,
    team_id bigint,
    tournament_id bigint,
    mask_results varchar(255),
    PRIMARY KEY(id),
    FOREIGN KEY(team_id) REFERENCES teams(id),
    FOREIGN KEY(tournament_id) REFERENCES tournaments(id)
);



CREATE TABLE IF NOT EXISTS leagues_tournaments(
	id bigint NOT NULL AUTO_INCREMENT,
	league_id bigint,
    tournament_id bigint,
    PRIMARY KEY(id),
    FOREIGN KEY(league_id) REFERENCES leagues(id),
    FOREIGN KEY(tournament_id) REFERENCES tournaments(id)
);

CREATE TABLE IF NOT EXISTS team_compositions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tournament_id BIGINT,
    parent_team_id BIGINT,
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY (parent_team_id) REFERENCES teams(id)
);

CREATE TABLE IF NOT EXISTS team_compositions_players (
    team_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    PRIMARY KEY (team_id, player_id)
);

