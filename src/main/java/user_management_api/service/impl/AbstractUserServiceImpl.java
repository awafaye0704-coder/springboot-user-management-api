package user_management_api.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.techqueen.digital.keycloak.models.UserKeycloak;
import sn.techqueen.digital.keycloak.services.AbstractUserService;
import user_management_api.dto.UserDto;
import user_management_api.entity.User;
import user_management_api.mapper.UserMapper;
import user_management_api.repository.UserRepository;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractUserServiceImpl implements AbstractUserService<User, UserDto> {
    final UserRepository userRepository;

    final UserMapper userMapper;

    static final String USER = "user";

    @Override
    public UserKeycloak asUserKeycloak(UserDto userDto) {
        log.trace("Converting UserDto to UserKeycloak: {}", userDto);
        return userMapper.asUserKeycloak(userDto);
    }

    @Override
    public boolean existsByUsername(String username) {
        log.trace("Checking the existence of the username: {}", username);
        boolean exists = userRepository.existsByUsername(username);
        return exists;
    }
    @Override
    public boolean existsByUserEmailAddress(String userEmailAddress) {
        return userRepository.existsByUserEmailAddress(userEmailAddress);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto readUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);

        return userMapper.toDto(user);
    }

    @Override
    public boolean existsByUserEmailAddressAndUserIdNot(
            String userEmailAddress,
            Long userId) {

        return userRepository
                .findByUserEmailAddress(userEmailAddress)
                .filter(user -> !user.getUserId().equals(userId))
                .isPresent();
    }
}
