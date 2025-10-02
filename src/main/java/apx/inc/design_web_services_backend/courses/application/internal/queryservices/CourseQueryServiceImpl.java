package apx.inc.design_web_services_backend.courses.application.internal.queryservices;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.courses.domain.model.queries.*;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import apx.inc.design_web_services_backend.courses.domain.services.CourseQueryService;
import apx.inc.design_web_services_backend.courses.infrastructure.persistence.jpa.repositories.CourseRepository;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseQueryServiceImpl implements CourseQueryService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseQueryServiceImpl(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Course> handle(GetAllCoursesQuery getAllCoursesQuery) {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> handle(GetCourseByIdQuery query) {
        return courseRepository.findById(query.courseId());
    }

    @Override
    public Optional<CourseJoinCode> handle(GetCourseJoinCodeById getCourseJoinCodeByIdQuery) {
        var optionalCourse= courseRepository.findById(getCourseJoinCodeByIdQuery.courseId());

        if(optionalCourse.isEmpty()){
            throw new IllegalArgumentException("Course not found");
        }

        var courseJoinCode = optionalCourse.get().getCourseJoinCode();

        return Optional.of(courseJoinCode);
    }

    @Override
    public List<Course> handle(GetCoursesByStudentIdQuery getCoursesByStudentIdQuery) {

        //1. Validamos el user
        var optionalStudent= userRepository.findById(getCoursesByStudentIdQuery.studentId());

        if(optionalStudent.isEmpty()){
            throw new IllegalArgumentException("Student not found");
        }

        //2. Validamos que el user sea un student

        if (optionalStudent.get().getUserRoles().stream().noneMatch(role -> role.getName().equals(Roles.ROLE_STUDENT))) {
            throw new IllegalArgumentException("User is not a student");
        }

        var student= optionalStudent.get();

        //3. Devolvemos los courses que coinciden con la lista del student

        var optionalCourseList= student.getStudentInCourses().stream().toList();

        if(optionalCourseList.isEmpty()){
            throw new IllegalArgumentException("No courses assigned to this student");
        }

        return optionalCourseList;
    }

    public List<Course> handle(GetCoursesByTeacherIdQuery query) {

        return courseRepository.findByTeacherId(query.teacherId());

    }
}
