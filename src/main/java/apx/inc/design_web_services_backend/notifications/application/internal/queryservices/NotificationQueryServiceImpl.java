package apx.inc.design_web_services_backend.notifications.application.internal.queryservices;

import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetAllNotificationsQuery;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetNotificationByIdQuery;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetNotificationsByUserIdQuery;
import apx.inc.design_web_services_backend.notifications.domain.services.NotificationQueryService;
import apx.inc.design_web_services_backend.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    @Override
    public Optional<Notification> handle(GetNotificationByIdQuery getNotificationByIdQuery) {
        return notificationRepository.findById(getNotificationByIdQuery.notificationId());
    }

    @Override
    public List<Notification> handle(GetAllNotificationsQuery getAllNotificationsQuery) {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> handle(GetNotificationsByUserIdQuery getNotificationsByUserIdQuery) {
        return notificationRepository.findByUserId(getNotificationsByUserIdQuery.userId());
    }
}
