package com.studleague.studleague.services.interfaces;


import com.studleague.studleague.entities.SystemResult;

import java.util.List;

public interface SystemResultService {

    SystemResult findById(Long id);

    SystemResult findByName(String name);

    List<SystemResult> findAll();

    void deleteById(Long id);

    void deleteAll();


}
