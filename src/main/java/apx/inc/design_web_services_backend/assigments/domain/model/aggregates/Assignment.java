package apx.inc.design_web_services_backend.assigments.domain.model.aggregates;


import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateAssignmentCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.entities.Submission;
import apx.inc.design_web_services_backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Getter
@Entity
public class Assignment extends AuditableAbstractAggregateRoot<Assignment> {

    private String title;
    private String description;
    private Long courseId;
    private Date deadline;
    private String imageUrl;

    // Relación One-to-Many con Submission
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Submission> submissions = new ArrayList<>();

    // NUEVO: Lista para archivos adicionales
    @ElementCollection
    @CollectionTable(name = "assignment_files", joinColumns = @JoinColumn(name = "assignment_id"))
    @Column(name = "file_url")
    private List<String> fileUrls = new ArrayList<>();

    protected Assignment() {
        super();
    }

    public Assignment(CreateAssignmentCommand createAssignmentCommand) {
        this.title = createAssignmentCommand.title();
        this.description = createAssignmentCommand.description();
        this.courseId = createAssignmentCommand.courseId();
        this.deadline = createAssignmentCommand.deadline();
        this.imageUrl = generatePicsumImageUrl(createAssignmentCommand.title());
    }

    public Assignment updateInformation(String title, String description, Long courseId, Date deadline, String imageUrl) {
        this.title = title;
        this.description = description;
        this.courseId = courseId;
        this.deadline = deadline;
        this.imageUrl = imageUrl;
        return this;
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
