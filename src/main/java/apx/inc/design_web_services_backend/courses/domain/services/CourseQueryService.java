package apx.inc.design_web_services_backend.courses.domain.services;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.courses.domain.model.queries.*;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;

import java.util.List;
import java.util.Optional;

public interface CourseQueryService {
    List<Course> handle(GetAllCoursesQuery getAllCoursesQuery);

    Optional<Course> handle(GetCourseByIdQuery getCourseByIdQuery);

    Optional<CourseJoinCode> handle(GetCourseJoinCodeById getCourseJoinCodeByIdQuery);

    List<Course> handle(GetCoursesByUserIdQuery getCoursesByUserIdQuery);

    List<Course> handle(GetCoursesByTeacherIdQuery getCoursesByTeacherIdQuery);

}
