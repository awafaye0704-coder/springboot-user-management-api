package user_management_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import sn.techqueen.digital.keycloak.mappers.EntityMapper;
import sn.techqueen.digital.keycloak.models.UserKeycloak;
import user_management_api.dto.UserDto;
import user_management_api.entity.User;
import user_management_api.properties.RealmProperties;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper implements EntityMapper<User, UserDto> {

    @Autowired
    protected RealmProperties realmProperties;

    public abstract UserDto toDto(User user);

    public abstract User toEntity(UserDto userDto);

    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "userRealm", expression = "java(realmProperties.realm())")
    public abstract UserKeycloak asUserKeycloak(UserDto user);
}
