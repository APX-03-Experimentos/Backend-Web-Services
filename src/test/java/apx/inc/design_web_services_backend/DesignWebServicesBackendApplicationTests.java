package apx.inc.design_web_services_backend;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateAssignmentCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.entities.Submission;
import apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories.AssignmentRepository;
import apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories.SubmissionRepository;
import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.courses.domain.model.commands.UpdateCourseCommand;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import apx.inc.design_web_services_backend.courses.infrastructure.persistence.jpa.repositories.CourseRepository;
import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DesignWebServicesBackendApplicationTests {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Verify that you can create an Assignment and a Submission and that they are correctly saved in the database.
    @Test
    @Transactional
    void createAssignmentAndSubmission_shouldPersistCorrectly() {

        CreateAssignmentCommand assignmentCmd = new CreateAssignmentCommand("Test Assignment", "Description", 1L, new Date(System.currentTimeMillis() + 1000000), "img.png");
        Assignment assignment = new Assignment(assignmentCmd);
        assignmentRepository.save(assignment);

        CreateSubmissionCommand submissionCmd = new CreateSubmissionCommand(assignment.getId(), 2L, "My solution", "submission.png"
        );
        Submission submission = new Submission(assignment, submissionCmd);
        submissionRepository.save(submission);

        Assignment fetched = assignmentRepository.findById(assignment.getId()).orElseThrow();
        List<Submission> submissions = submissionRepository.findByAssignmentId(assignment.getId());

        assertThat(fetched.getTitle()).isEqualTo("Test Assignment");
        assertThat(submissions).hasSize(1);
        assertThat(submissions.get(0).getContent()).isEqualTo("My solution");
    }

    // Verify that you can create a Course, update it, assign a CourseJoinCode, and that everything is correctly saved in the database.
    @Test
    @Transactional
    void createAndUpdateCourse_shouldPersistCorrectly() {
        Course course = new Course("Open Source", "img.png", 1L);
        courseRepository.save(course);

        UpdateCourseCommand updateCmd = new UpdateCourseCommand(course.getId(), "Web App", "new_img.png"
        );
        course.updateCourse(updateCmd);
        courseRepository.save(course);

        CourseJoinCode joinCode = new CourseJoinCode("ABCDEFGH", new Date());
        course.setJoinCode(joinCode);
        courseRepository.save(course);

        Course fetchedWithCode = courseRepository.findById(course.getId()).orElseThrow();
        assertThat(fetchedWithCode.getCourseJoinCode().key()).isEqualTo("ABCDEFGH");
    }

    // Verify that you can create a User, assign Roles to it, and that everything is correctly saved in the database.
    @Test
    @Transactional
    void createUserAndAssignRoles_shouldPersistCorrectly() {
        User user = new User("omar", "12345");

        Role admin = roleRepository.findByName(Roles.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("El rol no existe"));

        user.addRoles(List.of(admin));
        userRepository.save(user);
    }

    // Verify that you can assign a User to a Course and that the relationship is correctly persisted in the database.
    @Test
    @Transactional
    void assignUserToCourse_shouldPersistRelation() {
        User user = new User("jose", "123");
        userRepository.save(user);

        Course course = new Course("Open Source", "img.png", 1L);
        courseRepository.save(course);

        user.assignToCourse(course);
        userRepository.save(user);

        User fetchedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(fetchedUser.getStudentInCourses()).contains(course);
    }
}
