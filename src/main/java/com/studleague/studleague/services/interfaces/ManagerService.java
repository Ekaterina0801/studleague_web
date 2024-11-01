package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.security.User;
import org.apache.poi.ss.formula.functions.T;

public interface ManagerService {

   boolean isManager(Long userId, Long entityId);
}
