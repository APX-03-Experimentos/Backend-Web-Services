package apx.inc.design_web_services_backend.assigments.domain.model.aggregates;


import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateAssignmentCommand;
import apx.inc.design_web_services_backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
public class Assignment extends AuditableAbstractAggregateRoot<Assignment> {

    private String title;
    private String description;
    private Long courseId;
    private Date deadline;
    private String imageUrl;

    protected Assignment() {
        super();
    }

    public Assignment(CreateAssignmentCommand createAssignmentCommand) {
        this.title = createAssignmentCommand.title();
        this.description = createAssignmentCommand.description();
        this.courseId = createAssignmentCommand.courseId();
        this.deadline = createAssignmentCommand.deadline();
        this.imageUrl = createAssignmentCommand.imageUrl();
    }

    public Assignment updateInformation(String title, String description, Long courseId, Date deadline, String imageUrl) {
        this.title = title;
        this.description = description;
        this.courseId = courseId;
        this.deadline = deadline;
        this.imageUrl = imageUrl;
        return this;
    }
}
