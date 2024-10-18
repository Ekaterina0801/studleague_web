package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.Transfer;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FlagService {
    List<Flag> getAllFlags();

    void saveFlag(Flag flag);

    Flag getFlagById(long id);

    void deleteFlag(long id);

}
