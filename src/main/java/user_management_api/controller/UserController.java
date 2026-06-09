package user_management_api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import user_management_api.dto.UserDto;
import user_management_api.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "/users", description = "users  controllers")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create User", description = "Create an user in the User Manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(
            @Parameter(description = "User to create", required = true)
            @Valid @RequestBody UserDto userDto) {
        userDto.setUserId(null);
        userDto = userService.createUser(userDto);
        return userDto;
    }

    @Operation(summary = "Read an user by his identifier", description = "Read an user in the User Manager by his identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping(value = "/{userId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserDto readUser(@Parameter(description = "User identifier", name = "userId", required = true) @PathVariable("userId") Long userId) {

        return userService.readUserByUserId(userId);
    }

    @Operation(summary = "Read an user by username",
            description = "Read an user in the User Manager by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/username/{username}", produces = "application/json")
    public UserDto readUserByUsername(
            @Parameter(name = "username", required = true) @PathVariable(value = "username") String username) {

        return userService.readUserByUsername(username);
    }

    @Operation(summary = "Read users by keycloak realm", description = "Read users in the User Manager by keycloak realm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping
    public Page<UserDto> readUsers(
            @Parameter(description = "Filter user with email containing this value (case insensitive)") @RequestParam(required = false) String email,
            @Parameter(name = "exceptedRoles") @RequestParam(value = "exceptedRoles", required = false) List<String> exceptedRoles,
            @Parameter(description = "Filter user with first containing this value (case insensitive)") @RequestParam(required = false) String firstName,
            @Parameter(description = "Filter user with last name containing this value (case insensitive)") @RequestParam(required = false) String lastName,
            @Parameter(description = "Filter users with list of roles", example = "{ADMIN, INVESTMENT_OFFICER}") @RequestParam(name = "roles", required = false) List<String> roles,
            Pageable pageable) {

        return userService.readUsers(email, firstName, lastName, roles, exceptedRoles, pageable);
    }

    @Operation(summary = "Update an user", description = "Update an user in the User Manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PutMapping(value = "/{userId}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(
            @Parameter(description = "User identifier to update", required = true) @PathVariable("userId") Long userId,
            @Valid
            @Parameter(description = "User to update", required = true) @RequestBody UserDto userDto) {

        userDto.setUserId(userId);

        return userService.updateUser(userDto);
    }

    @Operation(summary = "Read users by list ids", description = "Read users in the User Manager by list id user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @GetMapping(value = "/readByIds")
    public List<UserDto> readUsersByIds(@Parameter(name = "listIds") @RequestParam(value = "listIds") List<Long> listIds) {
        return userService.readUsersByIds(listIds);
    }

    @Operation(summary = "Delete an user", description = "Delete an user in the User Manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@Parameter(description = "User identifier to delete", required = true) @PathVariable("userId") Long userId) {

        /* Deleting user */
        userService.deleteUser(userId);
    }
}
