package user_management_api.properties;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "swagger.ui.oauth")
public record SwaggerUiProperties(

        @NotEmpty
        String clientId,

        @NotEmpty
        String clientSecret,

        @NotEmpty
        List<Realms> realms

) {

    @Validated
    public record Realms(
            @NotEmpty
            String name,
            @NotEmpty
            String tokenUrl
    ) {
    }
}
