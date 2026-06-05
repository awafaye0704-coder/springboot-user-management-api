package user_management_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
@TableGenerator(name = "UserGen", table = "JPA_SEQUENCES", pkColumnName = "SEQ_KEY", valueColumnName = "SEQ_VALUE", pkColumnValue = "UserId", initialValue = 0, allocationSize = 1)
public class User implements Serializable {

    @Id
    @Column(name = "UserId", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "UserGen")
    private Long userId;

    @CreatedDate
    @Column(name = "UserCreationDate", updatable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime userCreationDate;

    @LastModifiedDate
    @Column(name = "UserModificationDate", insertable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime userModificationDate;

    @EqualsAndHashCode.Include
    @Column(name = "UserReference", nullable = false, updatable = false)
    private String userKeycloakId;

    @Column(name = "Username", updatable = false)
    @NotBlank(message = "Your User needs a username.")
    @EqualsAndHashCode.Include
    private String username;

    @Column(name = "UserLastName")
    @EqualsAndHashCode.Include
    private String userLastName;

    @Column(name = "UserFirstName")
    @EqualsAndHashCode.Include
    private String userFirstName;

    @EqualsAndHashCode.Include
    @Column(name = "UserEmailAddress")
    private String userEmailAddress;

    @Column(name = "UserLocale")
    private String userLocale;


    @Column(name = "enable")
    private boolean enable;

    @ManyToOne
    @JoinColumn(name = "RoleId", nullable = false)
    private Role role;


}
