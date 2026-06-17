package user_management_api.properties;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "realm.admin")
public record RealmProperties(

        @NotEmpty
        String realm,

        @NotEmpty
        String clientId
) {
}
