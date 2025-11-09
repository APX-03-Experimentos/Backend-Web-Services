package apx.inc.design_web_services_backend.notifications.domain.services;

import apx.inc.design_web_services_backend.notifications.domain.model.commands.CreateNotificationCommand;

public interface NotificationCommandService {

    Long handle(CreateNotificationCommand createNotificationCommand);
}
