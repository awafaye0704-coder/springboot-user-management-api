package user_management_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Role")
@EntityListeners(AuditingEntityListener.class)
@TableGenerator(name = "RoleGen", table = "JPA_SEQUENCES", pkColumnName = "SEQ_KEY", valueColumnName = "SEQ_VALUE", pkColumnValue = "RoleId", allocationSize = 1)
public class Role {

    @Id
    @Column(name = "RoleId", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RoleGen")
    Long roleId;

    @Column(name = "RoleName", nullable = false)
    String name;

    @Column(name = "RoleDescription")
    String description;

    @Column(name = "ExternalReference")
    String externalReference;

    @CreatedDate
    @Column(name = "RoleCreationDate", updatable = false, nullable = false)
    LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "RoleModificationDate", insertable = false)
    LocalDateTime modificationDate;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @JoinTable(name = "RolePermission", joinColumns = @JoinColumn(name = "RoleId"))
    @Column(name = "Permission", nullable = false)
    Collection<String> permissions;

}
