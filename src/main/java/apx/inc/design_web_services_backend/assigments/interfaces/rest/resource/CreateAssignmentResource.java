package apx.inc.design_web_services_backend.assigments.interfaces.rest.resource;

import java.util.Date;

public record CreateAssignmentResource(
        String title,
        String description,
        Long courseId,
        Date deadline,
        String imageUrl) {
    public CreateAssignmentResource {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Group ID cannot be null or less than 1");
        }
        if (deadline == null || deadline.before(new Date())) {
            throw new IllegalArgumentException("Deadline cannot be null or in the past");
        }
    }
}
