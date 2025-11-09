package apx.inc.design_web_services_backend.notifications.interfaces.rest.transform;

import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import apx.inc.design_web_services_backend.notifications.interfaces.rest.resources.NotificationResource;

public class NotificationResourceFromEntityAssembler {
    public static NotificationResource toResourceFromEntity(Notification notification){
        return new NotificationResource(
                notification.getId(),
                notification.getUserId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType().toString(),
                notification.getRead(),
                notification.getOcurredAt(),
                notification.getSourceCourseId(),
                notification.getSourceAssignmentId()
        );
    }
}
