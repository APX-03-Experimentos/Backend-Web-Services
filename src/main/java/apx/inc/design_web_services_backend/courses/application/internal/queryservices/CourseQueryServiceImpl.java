package apx.inc.design_web_services_backend.courses.application.internal.queryservices;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetAllCoursesQuery;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetCourseByIdQuery;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetCourseJoinCodeById;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetCoursesByUserIdQuery;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import apx.inc.design_web_services_backend.courses.domain.services.CourseQueryService;
import apx.inc.design_web_services_backend.courses.infrastructure.persistence.jpa.repositories.CourseRepository;
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
    public Optional<Course> handle(GetCourseByIdQuery getCourseByIdQuery,Long userId) {
        Optional<Course> optionalCourse = courseRepository.findById(getCourseByIdQuery.courseId());

        if (optionalCourse.isEmpty()) {
            throw new IllegalArgumentException("Course with ID " + getCourseByIdQuery.courseId() + " not found");
        }

        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        boolean belongsToGroup = userOpt.get().getStudentInCourses().stream()
                .anyMatch(c -> c.getId().equals(getCourseByIdQuery.courseId()));

        if (!belongsToGroup) {
            // El usuario no pertenece al grupo
            return Optional.empty();
        }

        return optionalCourse;
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
    public List<Course> handle(GetCoursesByUserIdQuery getCoursesByUserIdQuery) {

        //1. validamos el student
        var optionalUser= userRepository.findById(getCoursesByUserIdQuery.userId());

        if(optionalUser.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }

        var user= optionalUser.get();

        //2. devolvemos los courses que coinciden con la lista del student

        var optionalCourseList= user.getStudentInCourses().stream().toList();

        if(optionalCourseList.isEmpty()){
            throw new IllegalArgumentException("No courses assigned to this user");
        }

        return optionalCourseList;
    }
}
