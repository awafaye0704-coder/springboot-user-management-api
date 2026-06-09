
package user_management_api.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_management_api.dto.RoleDto;
import user_management_api.entity.Role;
import user_management_api.exception.ForbiddenActionException;
import user_management_api.mapper.RoleMapper;
import user_management_api.repository.RoleRepository;
import user_management_api.service.RoleService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    static final String ROLE_NAME_UPDATE_FORBIDDEN = "Role name cannot be updated.";

    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        log.info("Creating role: {}", roleDto.getName());

        Role role = roleMapper.asRole(roleDto);
        Role savedRole = roleRepository.save(role);

        return roleMapper.asRole(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto readRoleByRoleId(Long roleId) {
        log.info("Fetching role by id: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + roleId));

        return roleMapper.asRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto readRoleByRoleName(String roleName) {
        log.info("Fetching role by name: {}", roleName);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with name: " + roleName));

        return roleMapper.asRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleDto> readRolesByFilters(String name, String description, Pageable pageable) {
        log.info("Fetching roles with filters - name: {} - description: {}", name, description);

        return roleRepository.findByFilters(name, description, pageable)
                .map(roleMapper::asRole);
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto) {

        /* Getting role*/
        var existingRole = readRoleByRoleId(roleDto.getRoleId());

        /* Checking if name has changed */
        if (!existingRole.getName().equals(roleDto.getName())) {
            throw new ForbiddenActionException(HttpStatus.FORBIDDEN, ROLE_NAME_UPDATE_FORBIDDEN);
        }
        /* Setting external reference */
        roleDto.setExternalReference(existingRole.getExternalReference());
        roleDto.setModificationDate(LocalDateTime.now());

        /* Saving entity */
        var updatedRole = roleMapper.asRole(roleRepository.save(roleMapper.asRole(roleDto)));

        log.info("updateRole end ok -  name: {}", roleDto.getName());
        log.trace("updateRole end ok - roles: {}", updatedRole);


        return updatedRole;
    }

    @Override
    public void deleteRole(Long roleId) {
        log.info("Deleting role with id: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + roleId));

        if (isSystemRole(role.getName())) {
            throw new RuntimeException("Cannot delete system role: " + role.getName());
        }

        roleRepository.delete(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<String> getPermissions(String roleName) {
        log.info("Fetching permissions for role: {}", roleName);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with name: " + roleName));

        return role.getPermissions();
    }

    @Override
    public RoleDto addPermission(String roleName, String permission) {
        log.info("Adding permission '{}' to role '{}'", permission, roleName);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with name: " + roleName));

        if (role.getPermissions().contains(permission)) {
            throw new RuntimeException("Permission '" + permission + "' already exists on role: " + roleName);
        }

        role.getPermissions().add(permission);

        return roleMapper.asRole(roleRepository.save(role));
    }

    @Override
    public RoleDto deletePermission(String roleName, String permission) {
        log.info("Removing permission '{}' from role '{}'", permission, roleName);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with name: " + roleName));

        if (!role.getPermissions().contains(permission)) {
            throw new RuntimeException("Permission '" + permission + "' not found on role: " + roleName);
        }

        role.getPermissions().remove(permission);

        return roleMapper.asRole(roleRepository.save(role));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getPermissionsByRoleName(String roleName) {
        log.info("Fetching permissions list for role: {}", roleName);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with name: " + roleName));

        return List.copyOf(role.getPermissions());
    }

    // -------------------------
    // Private helpers
    // -------------------------

    private boolean isSystemRole(String roleName) {
        return List.of("ADMIN", "SUPER_ADMIN", "SYSTEM").contains(roleName.toUpperCase());
    }
}