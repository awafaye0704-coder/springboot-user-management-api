package user_management_api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import user_management_api.dto.RoleDto;
import user_management_api.service.RoleService;

@Data
@Slf4j
@RestController
@RequestMapping("/v1/roles")
@Tag(name = "/roles", description = "roles controllers")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Create role", description = "A role is a set of permissions that give access to product features")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDto createRole(
            @Parameter(description = "Role to create", required = true) @Valid @RequestBody RoleDto roleDto) {

        roleDto.setRoleId(null);

        /* Creating role */
        roleDto = roleService.createRole(roleDto);

        return roleDto;

    }

    @Operation(summary = "Read a role by its name", description = "A role is identified by its name and the realm where it belongs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "No role found for the given name."),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping(value = "/_name/{roleName}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDto readRoleByRoleName(
            @Parameter(description = "Name of the role", name = "roleName", required = true) @PathVariable(value = "roleName") String roleName) {

        /* Getting role */
        return roleService.readRoleByRoleName(roleName);
    }

    @Operation(summary = "Read a role by its id", description = "A role is identified by its id and the realm where it belongs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "No role found for the given name."),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping(value = "/{roleId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public RoleDto readRoleByRoleId(
            @Parameter(description = "Name of the role", name = "roleId", required = true) @PathVariable(value = "roleId") Long roleId) {

        /* Getting role */
        return roleService.readRoleByRoleId(roleId);
    }

    @Operation(summary = "Read paginated roles by filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Page<RoleDto> readRoles(
            @Parameter(name = "name") @RequestParam(value = "name", required = false) String name,
            @Parameter(name = "description") @RequestParam(value = "description", required = false) String description,
            Pageable pageable) {

        /* Getting roleList */
        return roleService.readRolesByFilters(name, description, pageable);

    }

    @Operation(summary = "Update a role", description = "Change role description and/or permissions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PutMapping(value = "/{roleId}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public RoleDto updateRole(
            @Parameter(description = "Role identifier to update", name = "roleId", required = true) @PathVariable("roleId") Long roleId,
            @Valid
            @Parameter(description = "Role to update", required = true) @RequestBody RoleDto roleDto) {

        /* Setting role ID */
        roleDto.setRoleId(roleId);

        /* Updating role */
        return roleService.updateRole(roleDto);
    }

    @Operation(summary = "Delete a role", description = "Delete the role in the module and the IAM if no one use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role is successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "No role found for the given identifier"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping(value = "/{roleId}", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(
            @Parameter(description = "Role identifier", name = "roleId", required = true) @PathVariable(value = "roleId") Long roleId) {

        /* Deleting role */
        roleService.deleteRole(roleId);
    }

    @Operation(summary = "Add a permission to an existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Role to update not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PutMapping(value = "/{roleName}/permissions/{permission}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDto addPermissionToRole(
            @Parameter(description = "Role to update identified by its name", name = "roleName", required = true) @PathVariable("roleName") String roleName,
            @Parameter(description = "Permission to add", name = "permission", required = true) @PathVariable("permission") String permission) {
        return roleService.addPermission(roleName, permission);
    }

    @Operation(summary = "Remove a permission to an existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Role to update not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @DeleteMapping(value = "/{roleName}/permissions/{permission}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDto removePermissionFromRole(
            @Parameter(description = "Role to update identified by its name", required = true) @PathVariable("roleName") String roleName,
            @Parameter(description = "Permission to remove", required = true) @PathVariable("permission") String permission) {
        return roleService.deletePermission(roleName, permission);
    }

}
