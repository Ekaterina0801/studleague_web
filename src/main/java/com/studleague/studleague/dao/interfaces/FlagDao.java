package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Flag;
import java.util.List;
import java.util.Optional;

public interface FlagDao {

    Optional<Flag> findById(long id);

    List<Flag> findAll();

    void save(Flag flag);

    void deleteById(long id);

    void deleteAll();

}
