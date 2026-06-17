package user_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.techqueen.digital.keycloak.models.UserDtoModel;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDto extends UserDtoModel {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Valid
    @NotNull
    RoleDto role;

    @EqualsAndHashCode.Include
    @NotBlank
    String username;

    String userPassword;

    @EqualsAndHashCode.Include
    @NotBlank
    String userLastName;

    @EqualsAndHashCode.Include
    @NotBlank
    String userFirstName;

    String userLocale;

    @EqualsAndHashCode.Include
    @NotBlank
    @Email
    String userEmailAddress;

    LocalDateTime userCreationDate;

    LocalDateTime userModificationDate;
}