package apx.inc.design_web_services_backend.assigments.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class AssignmentDeadlineCloseAlertEvent extends ApplicationEvent {
    private final Long assignmentId;
    private final String title;
    private final Long courseId;
    private final Date deadline;
    private final Long hoursRemaining;
    public final LocalDateTime alertedAt;

    public AssignmentDeadlineCloseAlertEvent(
            Object source,
            Long assignmentId,
            String title,
            Long courseId,
            Date deadline,
            Long hoursRemaining) {
        super(source);
        this.assignmentId = assignmentId;
        this.title = title;
        this.courseId = courseId;
        this.deadline = deadline;
        this.hoursRemaining = hoursRemaining;
        this.alertedAt = LocalDateTime.now();

    }

    public String getDescription() {
        return String.format("Assignment '%s' deadline is approaching! Only %d hours remaining. Due: %s",
                title, hoursRemaining, deadline.toString());
    }
}
