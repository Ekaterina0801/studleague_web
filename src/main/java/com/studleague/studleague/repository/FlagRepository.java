package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlagRepository extends JpaRepository<Flag, Long> {

}
