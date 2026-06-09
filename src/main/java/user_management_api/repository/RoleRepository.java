package user_management_api.repository;

import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import user_management_api.entity.QRole;
import user_management_api.entity.Role;
import user_management_api.utils.PageableUtils;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {
    boolean existsByName(String name);

    Optional<Role> findByName(String name);

    default Page<Role> findByFilters(String name, String description, Pageable pageable) {

        var builder = new BooleanBuilder();

        if (StringUtils.isNotEmpty(name)) {
            builder.and(QRole.role.name.containsIgnoreCase(name));
        }

        if (StringUtils.isNotEmpty(description)) {
            builder.and(QRole.role.description.containsIgnoreCase(description));
        }

        //var modifedPageable = PageableUtils.withNullsLast(pageable);
        return findAll(builder, pageable);
    }
}
