package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.Transfer;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FlagService {
    public List<Flag> getAllFlags();

    public void saveFlag(Flag flag);

    public Flag getFlagById(int id);

    public void updateFlag(Flag flag, String[] params);

    public void deleteFlag(int id);

    //public void getFlagsForTeam(int team_id);

    //public void setFlagForTeam(int flag_id, int team_id);
}
