package apx.inc.design_web_services_backend.assigments.domain.model.events;

import apx.inc.design_web_services_backend.assigments.domain.model.entities.Submission;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
public class AssignmentCreatedAlertEvent extends ApplicationEvent {

    private final Long assignmentId;
    private final String title;
    private final Long courseId;
    private final Date deadline;
    private final LocalDateTime createdAt;

    public AssignmentCreatedAlertEvent(
        Object source,
        Long assignmentId,
        String title,
        Long courseId,
        Date deadline
    ){
        super(source);
        this.assignmentId = assignmentId;
        this.title = title;
        this.courseId = courseId;
        this.deadline = deadline;
        this.createdAt = LocalDateTime.now();
    }

    public String getDescription() {
        return String.format(
                "New assignment created: %s for course %d. Deadline: %s",
                title,
                courseId,
                deadline.toString()
        );
    }


}
