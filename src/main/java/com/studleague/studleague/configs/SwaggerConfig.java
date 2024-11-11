package com.studleague.studleague.configs;


import groovyjarjarantlr4.v4.runtime.atn.ErrorInfo;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.PathItem;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "LeagueRating Api",
                description = "LeagueRating application", version = "0.0.1"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
public class SwaggerConfig {

    @Server
    @Bean
    public List<io.swagger.v3.oas.models.servers.Server> servers(@Value("app.publicUrl") String url) {
        io.swagger.v3.oas.models.servers.Server server = new io.swagger.v3.oas.models.servers.Server();
        server.url(url);
        server.description("Main api");
        return List.of(server);
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            List<String> excludedKeywords = List.of("entity", "search", "property", "profile");

            openApi.getPaths().entrySet().removeIf(entry -> {
                PathItem pathItem = entry.getValue();
                return pathItem.readOperations().stream().anyMatch(operation ->
                        operation.getTags().stream().anyMatch(tag ->
                                excludedKeywords.stream().anyMatch(tag.toLowerCase()::contains)));
            });

            var sharedErrorSchema = ModelConverters.getInstance()
                    .read(ErrorInfo.class).get(ErrorInfo.class.getSimpleName());
            if (sharedErrorSchema == null) {
                throw new IllegalStateException(
                        "Can't generate response for 4xx and 5xx errors");
            }

            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation ->
                    operation.getResponses().forEach((status, response) -> {
                        if ((status.startsWith("4") || status.startsWith("5")) && response.getContent() != null) {
                            response.getContent().forEach((code, mediaType) -> mediaType.setSchema(sharedErrorSchema));
                        }
                    })));
        };
    }



}
