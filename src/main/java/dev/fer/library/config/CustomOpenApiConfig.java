package dev.fer.library.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class CustomOpenApiConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
      .info(new Info().title("Book Library").description("Book Library API documentation"))
      //.addSecurityItem(new SecurityRequirement().addList("Authorization"))
      .components(
        new io.swagger.v3.oas.models.Components()
          .addSecuritySchemes("bearerAuth", new SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .description("JWT Auth Token")));
  }

  @Bean
  public OpenApiCustomizer openApiCustomizer() {
    return openApi -> openApi.getPaths().values().forEach(
      pathItem -> pathItem.readOperations().forEach(operation -> {
        boolean secured = operation.getSecurity() != null && !operation.getSecurity().isEmpty();
        if (secured) {
            operation.getResponses()
            .addApiResponse("403", new ApiResponse().description("Forbidden"));
        }
      })
    );
  }
}
