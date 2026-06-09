package user_management_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import user_management_api.dto.RoleDto;
import user_management_api.entity.Role;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RoleMapper {

    RoleDto asRole(Role role);

    Role asRole(RoleDto roleDto);

}
