package user_management_api.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_management_api.dto.UserDto;
import user_management_api.entity.User;
import user_management_api.mapper.UserMapper;
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

    @Override
    public UserDto createUser(UserDto userDto) {

        User user = userMapper.toEntity(userDto);

        user.setEnable(true);

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
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

        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setUserFirstName(userDto.getUserFirstName());
        user.setUserLastName(userDto.getUserLastName());
        user.setUserEmailAddress(userDto.getUserEmailAddress());
        user.setUserLocale(userDto.getUserLocale());
        user.setRole(userMapper.toEntity(userDto).getRole());

        User updatedUser = userRepository.save(user);

        return userMapper.toDto(updatedUser);
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
}