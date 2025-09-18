package apx.inc.design_web_services_backend.courses.interfaces.rest.transform;

import apx.inc.design_web_services_backend.courses.domain.model.commands.CreateCourseCommand;
import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.CreateCourseResource;

public class CreateCourseCommandFromResourceAssembler {
    public static CreateCourseCommand toCommandFromResource(CreateCourseResource resource,Long teacherId) {
        return new CreateCourseCommand(
                resource.title(),
                teacherId
        );
    }
}
