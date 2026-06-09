package user_management_api.repository;

import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import user_management_api.entity.QUser;
import user_management_api.entity.User;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findByUsername(String username);

    default Page<User> findByFilters(String email, String firstName, String lastName, List<String> roles, List<String> exceptedRoles, Pageable pageable) {
        var builder = new BooleanBuilder();

        if (StringUtils.isNotEmpty(email)) {
            builder.and(QUser.user.userEmailAddress.containsIgnoreCase(email));
        }

        if (StringUtils.isNotEmpty(firstName)) {
            builder.and(QUser.user.userFirstName.containsIgnoreCase(firstName));
        }

        if (StringUtils.isNotEmpty(lastName)) {
            builder.and(QUser.user.userLastName.containsIgnoreCase(lastName));
        }

        // Roles filter
        if (Objects.nonNull(roles)) {
            builder.and(QUser.user.role.name.in(roles));
        }

        if (CollectionUtils.isNotEmpty(exceptedRoles)) {
            builder.and(QUser.user.role.name.notIn(exceptedRoles));
        }

        return findAll(builder, pageable);
    }
}
