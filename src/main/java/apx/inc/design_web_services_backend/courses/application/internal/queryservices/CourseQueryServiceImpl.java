package apx.inc.design_web_services_backend.courses.application.internal.queryservices;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetAllCoursesQuery;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetCourseByIdQuery;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetCourseJoinCodeById;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetCoursesByUserIdQuery;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import apx.inc.design_web_services_backend.courses.domain.services.CourseQueryService;

import java.util.List;
import java.util.Optional;

public class CourseQueryServiceImpl implements CourseQueryService {
    @Override
    public List<Course> handle(GetAllCoursesQuery getAllCoursesQuery) {
        return List.of();
    }

    @Override
    public Optional<Course> handle(GetCourseByIdQuery getCourseByIdQuery) {
        return Optional.empty();
    }

    @Override
    public Optional<CourseJoinCode> handle(GetCourseJoinCodeById getCourseJoinCodeByIdQuery) {
        return Optional.empty();
    }

    @Override
    public List<Course> handle(GetCoursesByUserIdQuery getCoursesByUserIdQuery) {
        return List.of();
    }
}
