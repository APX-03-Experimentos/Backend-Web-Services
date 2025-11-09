package apx.inc.design_web_services_backend.notifications.interfaces.websocket;

import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private final Long id;
    private final String title;
    private final String message;
    private final String type;
    private final boolean read;
    private final LocalDateTime occurredAt;
    private final Long sourceCourseId;
    private final Long sourceAssignmentId;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.type = notification.getType().name();
        this.read = notification.getRead();
        this.occurredAt = notification.getOcurredAt();
        this.sourceCourseId = notification.getSourceCourseId();
        this.sourceAssignmentId = notification.getSourceAssignmentId();
    }
}
