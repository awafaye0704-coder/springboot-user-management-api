package user_management_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import user_management_api.dto.RoleDto;
import user_management_api.exception.ForbiddenActionException;

import java.util.Collection;
import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);

    RoleDto readRoleByRoleId(Long roleId);

    RoleDto readRoleByRoleName(String roleName);

    Page<RoleDto> readRolesByFilters(String name, String description, Pageable pageable);

    RoleDto updateRole(RoleDto roleDto) throws ForbiddenActionException;

    void deleteRole(Long roleId);

    Collection<String> getPermissions(String roleName);

    RoleDto addPermission(String roleName, String permission);

    RoleDto deletePermission(String roleName, String permission);

    List<String> getPermissionsByRoleName(String roleName);
}
