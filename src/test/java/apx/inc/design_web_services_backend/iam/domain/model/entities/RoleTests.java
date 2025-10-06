package apx.inc.design_web_services_backend.iam.domain.model.entities;

import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTests {

    // Test role creation sets correct name
    @Test
    void createRole_ShouldSetName() {
        Role role = new Role(Roles.ROLE_ADMIN);
        assertThat(role.getName()).isEqualTo(Roles.ROLE_ADMIN);
    }

    // Test default role is admin
    @Test
    void getDefaultRole_ShouldReturnAdminRole() {
        Role defaultRole = Role.getDefaultRole();
        assertThat(defaultRole.getName()).isEqualTo(Roles.ROLE_ADMIN);
    }

    // Test validating null or empty role set returns default role
    @Test
    void validateRoleSet_ShouldReturnDefaultIfNullOrEmpty() {
        List<Role> roles1 = Role.validateRoleSet(null);
        List<Role> roles2 = Role.validateRoleSet(List.of());

        assertThat(roles1).hasSize(1);
        assertThat(roles1.get(0).getName()).isEqualTo(Roles.ROLE_ADMIN);

        assertThat(roles2).hasSize(1);
        assertThat(roles2.get(0).getName()).isEqualTo(Roles.ROLE_ADMIN);
    }

    // Test validating non-empty role set returns same roles
    @Test
    void validateRoleSet_ShouldReturnSameRolesIfNotEmpty() {
        Role teacher = new Role(Roles.ROLE_TEACHER);
        List<Role> validated = Role.validateRoleSet(List.of(teacher));

        assertThat(validated).containsExactly(teacher);
    }
}
