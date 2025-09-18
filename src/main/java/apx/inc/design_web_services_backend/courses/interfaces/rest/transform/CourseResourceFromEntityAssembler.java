package apx.inc.design_web_services_backend.courses.interfaces.rest.transform;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.shared.interfaces.rest.resources.CourseJoinCodeResource;
import apx.inc.design_web_services_backend.shared.interfaces.rest.resources.CourseResource;

public class CourseResourceFromEntityAssembler {
    public static CourseResource toResourceFromEntity(Course entity) {
        return new CourseResource(
                entity.getId(),
                entity.getTeacherId(),
                entity.getTitle(),
                entity.getImageUrl(),
                entity.getCourseJoinCode().key()
        );
    }
}
