package apx.inc.design_web_services_backend.assigments.interfaces.rest.resource;

import java.util.Date;

public record AssignmentResource(
        Long id,
        String title,
        String description,
        Long courseId,
        Date deadline,
        String imageUrl) {
}
