package apx.inc.design_web_services_backend.assigments.domain.model.entities;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.valueobjects.States;
import apx.inc.design_web_services_backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    // NUEVO: Lista para archivos adicionales
    @ElementCollection
    @CollectionTable(name = "submission_files", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "file_url")
    private List<String> fileUrls = new ArrayList<>();

    protected Submission() {
        super();
    }

    public Submission(Assignment assignment,CreateSubmissionCommand command) {
        this.assignment = assignment;
        this.studentId = command.studentId();
        this.content = command.content();
        this.score = 0;
        this.imageUrl = generatePicsumImageUrl(command.content());
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

    public void gradeSubmission(int newScore) {
        this.score = newScore;
        this.state = States.GRADED;
    }

    public void changeState(States newState) {
        this.state = newState;
    }

    // Métodos para archivos adicionales
    public void addFileUrl(String fileUrl) {
        this.fileUrls.add(fileUrl);
    }

    public void removeFileUrl(String fileUrl) {
        this.fileUrls.remove(fileUrl);
    }


    private String generatePicsumImageUrl(String key) {
        // Generar un seed único basado en el título del curso
        int seed = key != null ? Math.abs(key.hashCode()) : new Random().nextInt(1000);

        // URL de Picsum con dimensiones y seed único
        return "https://picsum.photos/400/300?random=" + seed;
    }

}
