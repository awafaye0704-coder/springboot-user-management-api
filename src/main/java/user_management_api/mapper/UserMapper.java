package user_management_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import user_management_api.dto.UserDto;
import user_management_api.entity.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
