package dev.fer.library.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OpenApiCustomizer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;

class CustomOpenApiConfigTest {

  private final CustomOpenApiConfig config = new CustomOpenApiConfig();

  @Test
  void shouldBeCreated() {
    OpenAPI api = config.openAPI();

    assertThat(api).isNotNull();        
    assertThat(api.getComponents()).isNotNull();
    assertThat(api.getComponents().getSecuritySchemes()).isNotNull();
    assertThat(api.getComponents().getSecuritySchemes()).containsKey("bearerAuth");
  }

  @Test
  void shouldAdd403ApiResponse() {
    OpenApiCustomizer customizer = config.openApiCustomizer();

    OpenAPI openApi = new OpenAPI();

    Operation operation = new Operation().security(List.of(new SecurityRequirement()));

    operation.setResponses(new ApiResponses());

    PathItem pathItem = new PathItem().get(operation);

    openApi.path("/test", pathItem);

    customizer.customise(openApi);

    assertThat(operation.getResponses().get("403")).isNotNull();
  }

  @Test
  void shouldNotAdd403ApiResponse() {
    OpenApiCustomizer customizer = config.openApiCustomizer();

    OpenAPI openApi = new OpenAPI();

    Operation operation = new Operation();

    operation.setResponses(new ApiResponses());

    PathItem pathItem = new PathItem().get(operation);

    openApi.path("/test", pathItem);

    customizer.customise(openApi);

    assertThat(operation.getResponses().get("403")).isNull();
  }
}