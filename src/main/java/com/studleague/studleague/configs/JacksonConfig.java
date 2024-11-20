package com.studleague.studleague.configs;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Hibernate5Module hibernateModule = new Hibernate5Module();
        hibernateModule.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        mapper.registerModule(hibernateModule);
        mapper.enable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
        mapper.enable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(MapperFeature.USE_ANNOTATIONS, true);
        mapper.registerModule(new AfterburnerModule());
        mapper.configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, true);

        return mapper;
    }
}

