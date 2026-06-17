package user_management_api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.techqueen.digital.keycloak.properties.UserManagerConfiguration;
import sn.techqueen.digital.keycloak.services.AbstractUserService;
import sn.techqueen.digital.keycloak.services.KeycloakService;
import sn.techqueen.digital.keycloak.services.KeycloakUserService;
import user_management_api.dto.UserDto;
import user_management_api.entity.User;
import user_management_api.mapper.UserMapper;
import user_management_api.service.impl.AbstractUserServiceImpl;

@Configuration
public class KeycloakUserConfiguration   {
    @Bean
    public KeycloakUserService<User, UserDto> getKeycloakUserService(AbstractUserServiceImpl userService, UserMapper userMapper, KeycloakService keycloakService, UserManagerConfiguration userManagerConfiguration) {
        return new KeycloakUserService<>(userService, userMapper, keycloakService, userManagerConfiguration);
    }
}
