package apx.inc.design_web_services_backend.notifications.application.internal.commandservices;

import apx.inc.design_web_services_backend.notifications.application.internal.outboundservices.acl.ExternalIamService;
import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import apx.inc.design_web_services_backend.notifications.domain.model.commands.CreateNotificationCommand;
import apx.inc.design_web_services_backend.notifications.domain.services.NotificationCommandService;
import apx.inc.design_web_services_backend.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final ExternalIamService externalIamService;

    @Override
    public Long handle(CreateNotificationCommand createNotificationCommand) {

        //Validate the student exists
        var userOpt =externalIamService.fetchUserById(createNotificationCommand.userId());

        if(userOpt.isEmpty()){
            throw new IllegalArgumentException( "User with id " + createNotificationCommand.userId() + " does not exist");
        }

        //Create the notification
        var notification = new Notification(createNotificationCommand);

        try{
            notificationRepository.save(notification);
            return notification.getId();
        } catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
