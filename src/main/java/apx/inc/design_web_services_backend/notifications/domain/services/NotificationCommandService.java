package apx.inc.design_web_services_backend.notifications.domain.services;

import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import apx.inc.design_web_services_backend.notifications.domain.model.commands.CreateNotificationCommand;
import apx.inc.design_web_services_backend.notifications.domain.model.commands.MarkNotificationAsReadCommand;

import java.util.Optional;

public interface NotificationCommandService {

    Long handle(CreateNotificationCommand createNotificationCommand);

    Optional<Notification> handle(MarkNotificationAsReadCommand markNotificationAsReadCommand);
}
