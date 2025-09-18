package apx.inc.design_web_services_backend.assigments.domain.model.aggregates;


import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.valueobjects.States;
import apx.inc.design_web_services_backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Submission extends AuditableAbstractAggregateRoot<Submission> {


    //id en el auditable abstract aggregate root


    private Long assignmentId;
    private Long studentId;
    private String content;
    private int score;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private States state;

    protected Submission() {
        super();
    }

    /*
    public Submission(Long challengeId, Long studentId, String content, int score, String imageUrl) {
        this.challengeId = new ChallengeId(challengeId);
        this.studentId = new StudentId(studentId);
        this.content = new Content(content);
        this.score = new Score(score);
        this.imageUrl = imageUrl;
    }
    */

    public Submission(CreateSubmissionCommand command) {
        this.assignmentId = command.assignmentId();
        this.studentId = command.studentId();
        this.content = command.content();
        this.score = 0;
        this.imageUrl = command.imageUrl();
        this.state = States.NOT_GRADED; // Estado inicial
    }

    //Metodos que permiten actualizar el contenido y la puntuación de la submission

    public Submission updateSubmission(Long newAssignmentId, Long newStudentId, String newContent, int newScore, String newImageUrl) {
        this.assignmentId = newAssignmentId;
        this.studentId = newStudentId;
        this.content = newContent;
        this.score = newScore;
        this.imageUrl = newImageUrl;
        return this;
    }

    public Submission gradeSubmission(int newScore) {
        this.score = newScore;
        return this;
    }

    public Submission changeState(States newState) {
        this.state = newState;
        return this;
    }


}
