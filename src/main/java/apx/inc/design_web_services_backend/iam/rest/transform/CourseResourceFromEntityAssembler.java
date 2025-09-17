package apx.inc.design_web_services_backend.iam.rest.transform;

import apx.inc.design_web_services_backend.shared.interfaces.rest.resources.CourseResource;

public class CourseResourceFromEntityAssembler {
    public static CourseResource toResourceFromEntity(CourseResource courseResource) {
        return new CourseResource(
                courseResource.courseId(),
                courseResource.title(),
                courseResource.imageUrl(),
                courseResource.joinCode()
        );
    }
}
