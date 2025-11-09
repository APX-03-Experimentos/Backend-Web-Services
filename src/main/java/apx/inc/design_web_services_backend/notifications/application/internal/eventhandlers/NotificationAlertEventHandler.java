package apx.inc.design_web_services_backend.notifications.application.internal.eventhandlers;

import apx.inc.design_web_services_backend.assigments.domain.model.events.AssignmentCreatedAlertEvent;
import apx.inc.design_web_services_backend.assigments.domain.model.events.AssignmentDeadlineCloseAlertEvent;
import apx.inc.design_web_services_backend.assigments.domain.model.events.AssignmentFileAddedAlertEvent;
import apx.inc.design_web_services_backend.notifications.application.internal.outboundservices.acl.ExternalIamService;
import apx.inc.design_web_services_backend.notifications.domain.model.commands.CreateNotificationCommand;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetNotificationByIdQuery;
import apx.inc.design_web_services_backend.notifications.domain.model.valueobjects.NotificationType;
import apx.inc.design_web_services_backend.notifications.domain.services.NotificationCommandService;
import apx.inc.design_web_services_backend.notifications.domain.services.NotificationQueryService;
import apx.inc.design_web_services_backend.notifications.interfaces.websocket.NotificationWebSocketController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationAlertEventHandler {
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;
    private final ExternalIamService externalIamService;

    private final NotificationWebSocketController notificationWebSocketController;


    @EventListener(AssignmentCreatedAlertEvent.class)
    public void on(AssignmentCreatedAlertEvent event) {
        List<Long> studentsIds = externalIamService.getStudentsByCourseId(event.getCourseId());

        studentsIds.forEach(studentId -> {
            CreateNotificationCommand command =new CreateNotificationCommand(
                    studentId,
                    event.getTitle(),
                    event.getDescription(),
                    NotificationType.ASSIGNMENT_CREATED_ALERT,
                    event.getCourseId(),
                    event.getAssignmentId()
            );
            var notificationId = notificationCommandService.handle(command);

            sendNotificationViaWebSocket(studentId, notificationId);
        });
    }

    @EventListener(AssignmentFileAddedAlertEvent.class)
    public void on(AssignmentFileAddedAlertEvent event) {
        List<Long> studentsIds = externalIamService.getStudentsByCourseId(event.getCourseId());

        studentsIds.forEach(studentId -> {
            CreateNotificationCommand command =new CreateNotificationCommand(
                    studentId,
                    event.getTitle(),
                    event.getDescription(),
                    NotificationType.ASSIGNMENT_FILE_ADDED_ALERT,
                    event.getCourseId(),
                    event.getAssignmentId()
            );

            var notificationId = notificationCommandService.handle(command);

            sendNotificationViaWebSocket(studentId, notificationId);

        });
    }

    @EventListener(AssignmentDeadlineCloseAlertEvent.class)
    public void on(AssignmentDeadlineCloseAlertEvent event) {
        List<Long> studentsIds = externalIamService.getStudentsByCourseId(event.getCourseId());

        studentsIds.forEach(studentId -> {
            CreateNotificationCommand command =new CreateNotificationCommand(
                    studentId,
                    event.getTitle(),
                    event.getDescription(),
                    NotificationType.ASSIGNMENT_DEADLINE_ALERT,
                    event.getCourseId(),
                    event.getAssignmentId()
            );
            var notificationId=notificationCommandService.handle(command);

            sendNotificationViaWebSocket(studentId, notificationId);
        });
    }

    private void sendNotificationViaWebSocket(Long studentId, Long notificationId) {
        var notificationOpt = notificationQueryService.handle(new GetNotificationByIdQuery(notificationId));

        if (notificationOpt.isPresent()) {
            try {
                var notification = notificationOpt.get();
                notificationWebSocketController.sendNotificationToUser(studentId, notification);
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Notification with id " + notificationId + " could not be sent via WebSocket. " + e.getMessage()
                );
            }
        } else {
            throw new IllegalArgumentException("Notification with id " + notificationId + " not found");
        }
    }
}
