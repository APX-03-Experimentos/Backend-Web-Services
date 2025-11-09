package apx.inc.design_web_services_backend.notifications.interfaces.rest.resources;

import java.time.LocalDateTime;

public record NotificationResource(
        Long id,
        Long userId,
        String title,
        String message,
        String type,
        LocalDateTime ocurredAt,
        Long sourceCourseId,
        Long sourceAssignmentId
) {
}
