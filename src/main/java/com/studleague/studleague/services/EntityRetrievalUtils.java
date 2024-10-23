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

    public static <T> T getEntityByNameOrThrow(Optional<T> optionalEntity, String entityName, String name) {
        return optionalEntity.orElseThrow(() -> {
            logger.warn("{} with name {} not found", entityName, name);
            return new RuntimeException(entityName + " not found with name: " + name);
        });
    }

    public static <T> T getEntityByTwoIdOrThrow(Optional<T> optionalEntity, String entityName, long id1, long id2) {
        return optionalEntity.orElseThrow(() -> {
            logger.warn("{} with id1 {} and id2 {} not found", entityName, id1, id2);
            return new RuntimeException(entityName + " not found with ids: " + id1+" "+id2);
        });
    }

}


