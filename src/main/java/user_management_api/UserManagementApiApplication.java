package user_management_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ConfigurationPropertiesScan({
		"user_management_api.properties",
		"sn.techqueen.digital.keycloak.properties"
})
@SpringBootApplication(
		scanBasePackages = {
				"user_management_api",
				"sn.techqueen.digital.keycloak"
		}
)
public class UserManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApiApplication.class, args);
	}
}