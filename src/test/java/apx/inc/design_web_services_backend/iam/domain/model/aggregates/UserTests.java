package apx.inc.design_web_services_backend.iam.domain.model.aggregates;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.iam.domain.model.commands.UpdateUserCommand;
import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTests {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("omar", "12345");
    }

    @Test
    void createUser_ShouldSetUsernameAndPassword() {
        assertThat(user.getUserName()).isEqualTo("omar");
        assertThat(user.getPassword()).isEqualTo("12345");
    }

    @Test
    void updateUserDetails_ShouldUpdateUsernameAndPassword() {
        UpdateUserCommand command = new UpdateUserCommand("jose", "123");
        user.updateUserDetails(command);

        assertThat(user.getUserName()).isEqualTo("jose");
        assertThat(user.getPassword()).isEqualTo("123");
    }

    @Test
    void addRoles_ShouldAddValidRoles() {
        Role teacher = new Role(Roles.ROLE_TEACHER);
        user.addRoles(List.of(teacher));

        assertThat(user.getUserRoles()).contains(teacher);
    }

    @Test
    void assignAndRemoveCourse_ShouldModifyStudentCourses() {
        Course math = new Course("Open Source", "image.png", 1L);
        ReflectionTestUtils.setField(math, "id", 1L);

        user.assignToCourse(math);
        assertThat(user.getStudentInCourses()).contains(math);

        user.removeFromCourse(1L);
        assertThat(user.getStudentInCourses()).isEmpty();
    }
}