package user_management_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import user_management_api.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto readUserByUserId(Long userId);

    UserDto readUserByUsername(String username);

    Page<UserDto> readUsers(String email, String firstName, String lastName, List<String> roles, List<String> exceptedRoles, Pageable pageable);

    UserDto updateUser(UserDto userDto);

    void deleteUser(Long userId);

    List<UserDto> readUsersByIds(List<Long> listIds);
}
