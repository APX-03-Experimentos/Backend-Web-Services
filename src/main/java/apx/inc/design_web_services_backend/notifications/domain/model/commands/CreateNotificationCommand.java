package apx.inc.design_web_services_backend.notifications.domain.model.commands;

import apx.inc.design_web_services_backend.notifications.domain.model.valueobjects.NotificationType;

import java.time.LocalDateTime;

public record CreateNotificationCommand(
        Long userId,
        String title,
        String message,
        NotificationType type,// "ASSIGNMENT_CREATED_ALERT", "ASSIGNMENT_FILE_ADDED_ALERT", "ASSIGNMENT_DEAD_LINE_ALERT".


        Long sourceCourseId,
        Long sourceAssignmentId
) {
}
