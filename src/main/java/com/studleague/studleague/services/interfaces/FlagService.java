package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.FlagDTO;
import com.studleague.studleague.entities.Flag;


import java.util.List;

public interface FlagService {
    List<Flag> getAllFlags();

    void saveFlag(Flag flag);

    Flag getFlagById(Long id);

    void deleteFlag(Long id);

    void deleteAllFlags();

    boolean isManager(Long userId, Long flagId);

    boolean isManager(Long userId, FlagDTO flagDTO);

}
