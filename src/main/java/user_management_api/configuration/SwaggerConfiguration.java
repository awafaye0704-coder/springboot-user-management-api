package user_management_api.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user_management_api.properties.SwaggerUiProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {


    private static final String BASE_PACKAGE = "user_management_api.controller";
    private final SwaggerUiProperties swaggerUiProperties;
    @Value("${info.version}")
    String infoVersion;

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("All").addOpenApiCustomizer(openApi -> openApi.setServers(List.of(new Server().url(""))))
                .packagesToScan(BASE_PACKAGE)
                .build();
    }

    @Bean
    public OpenAPI openApi() {
        var openAPI = new OpenAPI().info(new Info().title("User Manager API")
                .description("Service that exposes REST API to access data.")
                .version(infoVersion));

        this.addSecurity(openAPI);

        return openAPI;
    }

    private void addSecurity(OpenAPI openApi) {
        Components components = createComponentsWithSecurityScheme();
        openApi.setComponents(components);
        List<SecurityRequirement> list = new ArrayList<>();
        for (SwaggerUiProperties.Realms realms : swaggerUiProperties.realms()) {
            SecurityRequirement securityRequirement = new SecurityRequirement().addList(realms.name());
            list.add(securityRequirement);
        }
        openApi.setSecurity(list);
    }

    private Components createComponentsWithSecurityScheme() {

        Components components = new Components();
        components.setSecuritySchemes(swaggerUiProperties.realms()
                .stream()
                .collect(Collectors.toMap((SwaggerUiProperties.Realms::name), this::createOpenIdScheme)));

        return components;
    }

    private SecurityScheme createOpenIdScheme(SwaggerUiProperties.Realms realms) {
        OAuthFlows flows = new OAuthFlows();
        OAuthFlow flow = new OAuthFlow();
        flow.setTokenUrl(realms.tokenUrl());

        flows = flows.password(flow);

        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

}
