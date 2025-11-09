package apx.inc.design_web_services_backend.notifications.domain.model.aggregates;

import apx.inc.design_web_services_backend.notifications.domain.model.commands.CreateNotificationCommand;
import apx.inc.design_web_services_backend.notifications.domain.model.valueobjects.NotificationType;
import apx.inc.design_web_services_backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Notification extends AuditableAbstractAggregateRoot<Notification> {

    private Long userId;
    private String title;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType type; // "ENVIRONMENTAL_ALERT", "SYSTEM", etc.
    private boolean read;
    private LocalDateTime ocurredAt;
    private Long sourceCourseId;
    private Long sourceAssignmentId;

    protected Notification() {
        super();// Constructor protegido para JPA
    }

    public Notification(CreateNotificationCommand command) {
        this.userId = command.userId();
        this.title = command.title();
        this.message = command.message();
        this.type = command.type();
        this.read = false;
        this.ocurredAt = LocalDateTime.now();
        this.sourceCourseId = command.sourceCourseId();
        this.sourceAssignmentId = command.sourceAssignmentId();
    }

    public void markAsRead() {
        this.read = true;
    }

    public boolean isUnread() {
        return !read;
    }

    public boolean getRead() {
        return  read;
    }
}