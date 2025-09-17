package apx.inc.design_web_services_backend.iam.rest.transform;

import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import apx.inc.design_web_services_backend.shared.interfaces.rest.resources.CourseJoinCodeResource;

public class CourseJoinCodeResourceFromEntityAssembler {
    public static CourseJoinCodeResource toResourceFromEntity(CourseJoinCode courseJoinCode) {
        return new CourseJoinCodeResource(
                courseJoinCode.key(),
                courseJoinCode.expiration()
        );
    }
}
