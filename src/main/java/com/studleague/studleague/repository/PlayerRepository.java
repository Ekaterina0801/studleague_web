package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByIdSite(long idSite);

    boolean existsByIdSite(long idSite);
}
