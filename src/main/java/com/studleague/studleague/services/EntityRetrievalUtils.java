package com.studleague.studleague.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class EntityRetrievalUtils {

    private static final Logger logger = LoggerFactory.getLogger(EntityRetrievalUtils.class);

    public static <T> T getEntityOrThrow(Optional<T> optionalEntity, String entityName, Long id) {
        return optionalEntity.orElseThrow(() -> {
            logger.warn("{} with id {} not found", entityName, id);
            return new RuntimeException(entityName + " not found with id: " + id);
        });
    }

}


