package apx.inc.design_web_services_backend.assignments.domain.model.entities;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateAssignmentCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.entities.Submission;
import apx.inc.design_web_services_backend.assigments.domain.model.valueobjects.States;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class SubmissionTests {

    // Verifica que una nueva Submission se inicializa correctamente a partir del comando CreateSubmissionCommand.
    @Test
    void createSubmission_shouldInitializeCorrectly() {
        Assignment assignment = new Assignment(new CreateAssignmentCommand(
                "Assignment 1", "Desc", 1L, new Date(System.currentTimeMillis() + 1000000), "img.png"));

        CreateSubmissionCommand command = new CreateSubmissionCommand(1L, 100L, "Submission content", "img.png");
        Submission submission = new Submission(assignment, command);

        assertThat(submission.getStudentId()).isEqualTo(100L);
        assertThat(submission.getContent()).isEqualTo("Submission 1");
        assertThat(submission.getScore()).isEqualTo(0);
        assertThat(submission.getState()).isEqualTo(States.NOT_GRADED);
    }

    // Verifica que al calificar una Submission, se actualiza el score y el estado cambia a GRADED.
    @Test
    void gradeSubmission_shouldSetScoreAndState() {
        Assignment assignment = new Assignment(new CreateAssignmentCommand(
                "Assignment 1", "Desc", 1L, new Date(System.currentTimeMillis() + 1000000), "img.png"));
        Submission submission = new Submission(assignment, new CreateSubmissionCommand(1L, 100L, "Submission content", "img.png"));

        submission.gradeSubmission(18);

        assertThat(submission.getScore()).isEqualTo(18);
        assertThat(submission.getState()).isEqualTo(States.GRADED);
    }

    // Verifica que se pueden agregar y eliminar URLs de archivos en la lista fileUrls correctamente.
    @Test
    void addAndRemoveFileUrl_shouldModifyFileList() {
        Assignment assignment = new Assignment(new CreateAssignmentCommand(
                "Assignment 1", "Desc", 1L, new Date(System.currentTimeMillis() + 1000000), "img.png"));
        Submission submission = new Submission(assignment, new CreateSubmissionCommand(1L, 100L, "Submission content", "img.png"));

        submission.addFileUrl("file1.docx");
        submission.addFileUrl("file2.docx");

        assertThat(submission.getFileUrls()).containsExactly("file1.docx", "file2.docx");

        submission.removeFileUrl("file1.docx");
        assertThat(submission.getFileUrls()).containsExactly("file2.docx");
    }
}
