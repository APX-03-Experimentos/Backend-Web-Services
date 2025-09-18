package apx.inc.design_web_services_backend.assigments.interfaces.rest.transform;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.AssignmentResource;

public class AssignmentResourceFromEntityAssembler {
    public static AssignmentResource toResourceFromEntity(Assignment entity) {
        return new AssignmentResource(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCourseId(),
                entity.getDeadline(),
                entity.getImageUrl()
        );
    }
}
