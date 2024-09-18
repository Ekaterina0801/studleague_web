package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.Transfer;

import java.util.List;

public interface FlagDao {
    Flag getFlagById(int id);

    List<Flag> getAllFlags();

    void saveFlag(Flag flag);

    void updateFlag(Flag flag, String[] params);

    void deleteFlag(int id);

}
