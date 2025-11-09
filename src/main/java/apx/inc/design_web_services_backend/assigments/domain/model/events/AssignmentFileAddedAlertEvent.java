package apx.inc.design_web_services_backend.assigments.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class AssignmentFileAddedAlertEvent extends ApplicationEvent {
    private final Long assignmentId;
    private final String title;
    private final Long courseId;
    private final LocalDateTime addedAt;

    public AssignmentFileAddedAlertEvent(
        Object source,
        Long assignmentId,
        String title,
        Long courseId
    ){
        super(source);
        this.assignmentId = assignmentId;
        this.title = title;
        this.courseId = courseId;
        this.addedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return "A new file has been added to the assignment: " + title;
    }

}
