package apx.inc.design_web_services_backend.notifications.application.internal.eventhandlers;

import apx.inc.design_web_services_backend.assigments.domain.model.events.AssignmentCreatedAlertEvent;
import apx.inc.design_web_services_backend.assigments.domain.model.events.AssignmentDeadlineCloseAlertEvent;
import apx.inc.design_web_services_backend.assigments.domain.model.events.AssignmentFileAddedAlertEvent;
import apx.inc.design_web_services_backend.notifications.application.internal.outboundservices.acl.ExternalIamService;
import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import apx.inc.design_web_services_backend.notifications.domain.model.commands.CreateNotificationCommand;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetNotificationByIdQuery;
import apx.inc.design_web_services_backend.notifications.domain.model.valueobjects.NotificationType;
import apx.inc.design_web_services_backend.notifications.domain.services.NotificationCommandService;
import apx.inc.design_web_services_backend.notifications.domain.services.NotificationQueryService;
import apx.inc.design_web_services_backend.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import apx.inc.design_web_services_backend.notifications.interfaces.websocket.NotificationWebSocketController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationAlertEventHandler {
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;
    private final NotificationRepository notificationRepository;
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
    @Transactional
    public void on(AssignmentDeadlineCloseAlertEvent event) {
        List<Long> studentsIds = externalIamService.getStudentsByCourseId(event.getCourseId());

        System.out.println("🔍 Procesando evento de deadline para assignment: " + event.getAssignmentId());
        System.out.println("👥 Estudiantes en el curso: " + studentsIds.size());

        // Obtener notificaciones existentes no leídas para este assignment
        List<Notification> existingUnreadNotifications = notificationRepository
                .findBySourceAssignmentIdAndReadFalse(event.getAssignmentId());

        System.out.println("📊 Notificaciones no leídas existentes: " + existingUnreadNotifications.size());

        // Extraer los IDs de usuarios que YA tienen notificación no leída
        Set<Long> alreadyNotifiedUserIds = existingUnreadNotifications.stream()
                .map(Notification::getUserId)
                .collect(Collectors.toSet());

        System.out.println("🎯 Estudiantes ya notificados: " + alreadyNotifiedUserIds);

        // Filtrar estudiantes que NO tienen notificación no leída
        List<Long> studentsToNotify = studentsIds.stream()
                .filter(studentId -> !alreadyNotifiedUserIds.contains(studentId))
                .toList();

        System.out.println("✅ Estudiantes a notificar: " + studentsToNotify.size());

        if (studentsToNotify.isEmpty()) {
            System.out.println("⏭️  Todos los estudiantes ya tienen notificación no leída");
            return;
        }

        // Crear notificaciones solo para los que no tienen
        studentsToNotify.forEach(studentId -> {
            System.out.println("📨 Creando notificación para estudiante: " + studentId);

            CreateNotificationCommand command = new CreateNotificationCommand(
                    studentId,
                    event.getTitle(),
                    event.getDescription(),
                    NotificationType.ASSIGNMENT_DEADLINE_ALERT,
                    event.getCourseId(),
                    event.getAssignmentId()
            );

            var notificationId = notificationCommandService.handle(command);
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
