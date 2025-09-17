package apx.inc.design_web_services_backend.courses.interfaces.rest.transform;

import apx.inc.design_web_services_backend.courses.domain.model.commands.UpdateCourseCommand;
import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.UpdateCourseResource;

public class UpdateCourseCommandFromResourceAssembler {
    public static UpdateCourseCommand toCommandFromResource(UpdateCourseResource resource, Long courseId) {
        return new UpdateCourseCommand(
                courseId,
                resource.title(),
                resource.imageUrl()
        );
    }
}
