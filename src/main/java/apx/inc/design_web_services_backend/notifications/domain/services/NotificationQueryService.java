package apx.inc.design_web_services_backend.notifications.domain.services;

import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetAllNotificationsQuery;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetNotificationByIdQuery;
import apx.inc.design_web_services_backend.notifications.domain.model.queries.GetNotificationsByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface NotificationQueryService {

    Optional<Notification> handle(GetNotificationByIdQuery getNotificationByIdQuery);

    List<Notification> handle(GetAllNotificationsQuery getAllNotificationsQuery);

    List<Notification> handle(GetNotificationsByUserIdQuery getNotificationsByVehicleIdQuery);

}
