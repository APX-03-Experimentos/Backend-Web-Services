package apx.inc.design_web_services_backend.notifications.interfaces.websocket;

import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NotificationWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Envía notificación a un usuario específico
     */
    public void sendNotificationToUser(Long userId, Notification notification) {
        String destination = "/topic/user/" + userId + "/notifications";
        messagingTemplate.convertAndSend(destination, notification);
        log.info("📤 WebSocket notification sent to user {}: {}", userId, notification.getTitle());
    }

    /**
     * Envía notificación a todos los usuarios de un curso
     */
    public void sendNotificationToCourse(Long courseId, Notification notification) {
        String destination = "/topic/course/" + courseId + "/notifications";
        messagingTemplate.convertAndSend(destination, notification);
        log.info("📤 WebSocket notification sent to course {}: {}", courseId, notification.getTitle());
    }

    /**
     * Envía notificación global (para admins o broadcast)
     */
    public void sendGlobalNotification(Notification notification) {
        String destination = "/topic/global/notifications";
        messagingTemplate.convertAndSend(destination, notification);
        log.info("📤 Global WebSocket notification sent: {}", notification.getTitle());
    }
}
