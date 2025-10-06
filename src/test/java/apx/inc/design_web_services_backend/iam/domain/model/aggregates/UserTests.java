package apx.inc.design_web_services_backend.iam.domain.model.aggregates;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.iam.domain.model.commands.UpdateUserCommand;
import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTests {

    // Test user creation with valid username and password
    @Test
    void createUser_ShouldSetUsernameAndPassword() {
        User user = new User("omar", "12345");

        assertThat(user.getUserName()).isEqualTo("omar");
        assertThat(user.getPassword()).isEqualTo("12345");
    }

    // Test updating username and password using UpdateUserCommand
    @Test
    void updateUserDetails_ShouldUpdateUsernameAndPassword() {
        User user = new User("oldUser", "1234");
        UpdateUserCommand command = new UpdateUserCommand("newUser", "abcd");

        user.updateUserDetails(command);

        assertThat(user.getUserName()).isEqualTo("newUser");
        assertThat(user.getPassword()).isEqualTo("abcd");
    }

    // Test adding roles to a user
    @Test
    void addRoles_ShouldAddValidRoles() {
        User user = new User("omar", "12345");
        Role teacher = new Role(Roles.ROLE_TEACHER);

        user.addRoles(List.of(teacher));

        assertThat(user.getUserRoles()).contains(teacher);
    }

    // Test assigning and removing courses from a user
    @Test
    void assignAndRemoveCourse_ShouldModifyStudentCourses() {
        User user = new User("omar", "12345");
        Course math = new Course("Math 101", "math.png", 1L);

        ReflectionTestUtils.setField(math, "id", 1L);

        user.assignToCourse(math);
        assertThat(user.getStudentInCourses()).contains(math);

        user.removeFromCourse(1L);
        assertThat(user.getStudentInCourses()).isEmpty();
    }
}
