package apx.inc.design_web_services_backend.courses.interfaces.rest.transform;

import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import apx.inc.design_web_services_backend.shared.interfaces.rest.resources.CourseJoinCodeResource;

public class CourseJoinCodeResourceFromEntityAssembler {
    public static CourseJoinCodeResource toResourceFromEntity(CourseJoinCode entity) {
        return new CourseJoinCodeResource(
                entity.key(),
                entity.expiration()
        );
    }
}
