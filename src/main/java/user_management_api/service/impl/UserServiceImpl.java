package user_management_api.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.techqueen.digital.keycloak.exceptions.ResourceNotFoundException;
import sn.techqueen.digital.keycloak.services.KeycloakUserService;
import user_management_api.dto.RoleDto;
import user_management_api.dto.UserDto;
import user_management_api.entity.User;
import user_management_api.mapper.RoleMapper;
import user_management_api.mapper.UserMapper;
import user_management_api.properties.RealmProperties;
import user_management_api.repository.RoleRepository;
import user_management_api.repository.UserRepository;
import user_management_api.service.UserService;

import java.util.List;

@Service
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    final KeycloakUserService<User, UserDto> keycloakUserService;
    final RoleMapper roleMapper;
    final RoleRepository roleRepository;

    static final String ROLE_NOT_FOUND_MESSAGE = "Role not found with the id: {0}";
    static final String ROLE_NOT_FOUND_BY_NAME_MESSAGE = "Role not found with the name: {0}";
    final RealmProperties realmProperties;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Creating user with details: {}", userDto);
        /* Getting role by id or name*/
        var role = getRole(userDto.getRole());

        /* Enable by default */
        userDto.setEnable(true);

        /* Setting role */
        userDto.setRole(role);

        return keycloakUserService.createUser(realmProperties.realm(), realmProperties.clientId(), userDto);

    }

    @Override
    public UserDto readUserByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id : " + userId));

        return userMapper.toDto(user);
    }

    @Override
    public UserDto readUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found with username : " + username));

        return userMapper.toDto(user);
    }

    @Override
    public Page<UserDto> readUsers(String email, String firstName, String lastName, List<String> roles, List<String> exceptedRoles, Pageable pageable) {

        log.info("Fetching users with filters - email: {} - firstName: {} - lastName: {} - role: {}", email, firstName, lastName, roles);

        /* Getting roles */
        var users = userRepository.findByFilters(email, firstName, lastName, roles, exceptedRoles, pageable).map(userMapper::toDto);

        log.trace("Users fetched: {}", users);

        return users;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("Updating user details: {}", userDto);

        /* Setting role */
        userDto.setRole(getRole(userDto.getRole()));

        /* Checking if ID exists */
        var userDb = readUserByUserId(userDto.getUserId());
        userDto.setUserKeycloakId(userDb.getUserKeycloakId());
        userDto.setUsername(userDb.getUsername());

        return keycloakUserService.updateUser(userDto);
    }

    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public List<UserDto> readUsersByIds(List<Long> listIds) {

        return userRepository.findAllById(listIds)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    private RoleDto getRole(RoleDto userRole) {
        if (userRole.getRoleId() != null) {
            return roleMapper.asRole(roleRepository.findById(userRole.getRoleId()).orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND_MESSAGE, userRole.getRoleId())));
        } else {
            return roleMapper.asRole(roleRepository.findByName(userRole.getName()).orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND_BY_NAME_MESSAGE, userRole.getName())));
        }
    }
}