package user_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long roleId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime creationDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime modificationDate;

    @NotBlank(message = "Your role needs a name.")
    @Size(min = 3, max = 100)
    @EqualsAndHashCode.Include
    String name;

    @EqualsAndHashCode.Include
    @Size(min = 3, max = 500)
    String description;

    String externalReference;

    Collection<String> permissions;
}
