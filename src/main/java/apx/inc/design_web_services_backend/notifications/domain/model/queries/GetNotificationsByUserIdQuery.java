package apx.inc.design_web_services_backend.notifications.domain.model.queries;

public record GetNotificationsByUserIdQuery(Long userId) {
    public GetNotificationsByUserIdQuery{
        if (userId==null || userId<=0){
            throw new IllegalArgumentException(" userId can not be null or less than equal to zero ");
        }
    }
}
