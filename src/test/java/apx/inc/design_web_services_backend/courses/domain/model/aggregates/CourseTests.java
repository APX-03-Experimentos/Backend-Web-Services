package apx.inc.design_web_services_backend.courses.domain.model.aggregates;

import apx.inc.design_web_services_backend.courses.domain.model.commands.CreateCourseCommand;
import apx.inc.design_web_services_backend.courses.domain.model.commands.UpdateCourseCommand;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseTests {

    // Test creating a course with a command
    @Test
    void createCourse_withCommand_createsProperly() {
        CreateCourseCommand command = new CreateCourseCommand("Open Source", 1L);
        Course course = new Course(command);

        assertThat(course.getTitle()).isEqualTo("Open Source");
        assertThat(course.getTeacherId()).isEqualTo(1L);
        assertThat(course.getCourseJoinCode()).isNotNull();
        assertThat(course.getImageUrl()).contains("https://picsum.photos");
    }

    // Test updating a course's title and image URL
    @Test
    void updateCourse_updatesTitleAndImageUrl() {
        Course course = new Course("Old Title", "old.png", 1L);
        UpdateCourseCommand command = new UpdateCourseCommand(1L, "New Title", "new.png");

        course.updateCourse(command);

        assertThat(course.getTitle()).isEqualTo("New Title");
        assertThat(course.getImageUrl()).isEqualTo("new.png");
    }

    // Test setting a join code
    @Test
    void setJoinCode_assignsCode() {
        Course course = new Course("Open Source", "img.png", 1L);
        CourseJoinCode joinCode = new CourseJoinCode("ABCDEFGH", new Date());

        course.setJoinCode(joinCode);

        assertThat(course.getCourseJoinCode()).isEqualTo(joinCode);
    }

    // Test resetting the join code
    @Test
    void resetJoinCode_setsCodeToNull() {
        Course course = new Course("Math 101", "img.png", 1L);
        CourseJoinCode joinCode = new CourseJoinCode("ABCDEFGH", new Date());
        course.setJoinCode(joinCode);

        course.resetJoinCode();

        assertThat(course.getCourseJoinCode()).isNull();
    }

    // Test generating a CourseJoinCode with null key
    @Test
    void courseJoinCode_withNullKey_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new CourseJoinCode(null, new Date()));
    }

    // Test generating a CourseJoinCode with empty key
    @Test
    void courseJoinCode_withEmptyKey_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new CourseJoinCode("", new Date()));
    }
}
