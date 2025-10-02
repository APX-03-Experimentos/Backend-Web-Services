package apx.inc.design_web_services_backend.assigments.domain.model.entities;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.valueobjects.States;
import apx.inc.design_web_services_backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Submission extends AuditableAbstractAggregateRoot<Submission> {

    // Referencia al Aggregate Root
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    private Long studentId;
    private String content;
    private int score;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private States state;

    protected Submission() {
        super();
    }

    public Submission(Assignment assignment,CreateSubmissionCommand command) {
        this.assignment = assignment;
        this.studentId = command.studentId();
        this.content = command.content();
        this.score = 0;
        this.imageUrl = command.imageUrl();
        this.state = States.NOT_GRADED; // Estado inicial
    }



    public Submission updateSubmission(Assignment assignment, Long newStudentId, String newContent, int newScore, String newImageUrl) {
        this.assignment = assignment;
        this.studentId = newStudentId;
        this.content = newContent;
        this.score = newScore;
        this.imageUrl = newImageUrl;
        return this;
    }

    public Submission gradeSubmission(int newScore) {
        this.score = newScore;
        this.state = States.GRADED;
        return this;
    }

    public Submission changeState(States newState) {
        this.state = newState;
        return this;
    }

}
