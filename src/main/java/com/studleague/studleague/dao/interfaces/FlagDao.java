package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.Transfer;

import java.util.List;
import java.util.Optional;

public interface FlagDao {

    Optional<Flag> getFlagById(int id);

    List<Flag> getAllFlags();

    void saveFlag(Flag flag);

    void deleteFlag(int id);

}
