create TABLE controversials (
  id BIGINT AUTO_INCREMENT NOT NULL,
   question_number INT NULL,
   answer VARCHAR(255) NULL,
   issued_at datetime NULL,
   status VARCHAR(255) NULL,
   comment VARCHAR(255) NULL,
   resolved_at VARCHAR(255) NULL,
   appeal_jury_comment VARCHAR(255) NULL,
   result_id BIGINT NULL,
   CONSTRAINT pk_controversials PRIMARY KEY (id)
);

create TABLE flags (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   league_id BIGINT NOT NULL,
   CONSTRAINT pk_flags PRIMARY KEY (id)
);

create TABLE full_results (
  id BIGINT AUTO_INCREMENT NOT NULL,
   team_id BIGINT NULL,
   tournament_id BIGINT NULL,
   mask_results VARCHAR(255) NULL,
   total_score DOUBLE NULL,
   CONSTRAINT pk_full_results PRIMARY KEY (id)
);

create TABLE leagues (
   id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   count_excluded_games INT NULL,
   user_id BIGINT NULL,
   system_result_id BIGINT NULL,
   CONSTRAINT pk_leagues PRIMARY KEY (id)
);

create TABLE leagues_tournaments (
  league_id BIGINT NOT NULL,
   tournament_id BIGINT NOT NULL
);

create TABLE players (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   patronymic VARCHAR(255) NULL,
   surname VARCHAR(255) NULL,
   university VARCHAR(255) NULL,
   date_of_birth date NULL,
   id_site BIGINT NULL,
   CONSTRAINT pk_players PRIMARY KEY (id)
);

create TABLE roles (
  id BIGINT NOT NULL,
   name VARCHAR(255) NOT NULL,
   CONSTRAINT pk_roles PRIMARY KEY (id)
);

create TABLE system_results (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   count_not_included_games INT NULL,
   `description` VARCHAR(255) NULL,
   CONSTRAINT pk_system_results PRIMARY KEY (id)
);

create TABLE team_compositions (
  id BIGINT AUTO_INCREMENT NOT NULL,
   parent_team_id BIGINT NOT NULL,
   tournament_id BIGINT NOT NULL,
   CONSTRAINT pk_teamcompositions PRIMARY KEY (id)
);

create TABLE team_compositions_players (
  player_id BIGINT NOT NULL,
   team_id BIGINT NOT NULL
);

create TABLE teams (
  id BIGINT AUTO_INCREMENT NOT NULL,
   team_name VARCHAR(255) NULL,
   university VARCHAR(255) NULL,
   id_site BIGINT NULL,
   league_id BIGINT NULL,
   CONSTRAINT pk_teams PRIMARY KEY (id)
);

create TABLE teams_flags (
  flag_id BIGINT NOT NULL,
   team_id BIGINT NOT NULL
);

create TABLE teams_players (
  player_id BIGINT NOT NULL,
   team_id BIGINT NOT NULL
);

create TABLE tournaments (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   id_site BIGINT NULL,
   date_of_start datetime NULL,
   date_of_final datetime NULL,
   CONSTRAINT pk_tournaments PRIMARY KEY (id)
);

create TABLE tournaments_players (
  player_id BIGINT NOT NULL,
   tournament_id BIGINT NOT NULL
);

create TABLE tournaments_teams (
  team_id BIGINT NOT NULL,
   tournament_id BIGINT NOT NULL
);

create TABLE transfers (
  id BIGINT AUTO_INCREMENT NOT NULL,
   transfer_date date NULL,
   player_id BIGINT NOT NULL,
   old_team_id BIGINT NOT NULL,
   new_team_id BIGINT NOT NULL,
   comments VARCHAR(255) NULL,
   CONSTRAINT pk_transfers PRIMARY KEY (id)
);

create TABLE users (
  id BIGINT NOT NULL,
   username VARCHAR(255) NULL,
   password VARCHAR(255) NULL,
   fullname VARCHAR(255) NULL,
   email VARCHAR(255) NULL,
   role_id BIGINT NULL,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

create TABLE users_leagues (
  league_id BIGINT NOT NULL,
   user_id BIGINT NOT NULL
);

create TABLE IF NOT EXISTS `users_seq` (
  `next_val` bigint DEFAULT NULL
)
