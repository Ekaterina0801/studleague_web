package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Flag;
import java.util.List;
import java.util.Optional;

public interface FlagDao {

    Optional<Flag> getFlagById(long id);

    List<Flag> getAllFlags();

    void saveFlag(Flag flag);

    void deleteFlag(long id);

    void deleteAll();

}
