package apx.inc.design_web_services_backend.assignments.domain.model.aggregates;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateAssignmentCommand;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssignmentTests {

    // Verifica que al crear un Assignment usando CreateAssignmentCommand,
    // todos los campos se inicializan correctamente.
    @Test
    void createAssignment_withCommand_createsProperly() {
        CreateAssignmentCommand command = new CreateAssignmentCommand(
                "Tarea 1", "Desc", 1L, new Date(System.currentTimeMillis() + 1000000), "img.png");
        Assignment assignment = new Assignment(command);

        assertThat(assignment.getTitle()).isEqualTo("Tarea 1");
        assertThat(assignment.getCourseId()).isEqualTo(1L);
        assertThat(assignment.getDescription()).isEqualTo("Desc");
        assertThat(assignment.getImageUrl()).isEqualTo("img.png");
    }

    // Verifica que el metodo updateInformation actualiza correctamente
    // el título, la descripción, el courseId y la URL de imagen del Assignment.
    @Test
    void updateInformation_shouldChangeFields() {
        Assignment assignment = new Assignment(new CreateAssignmentCommand(
                "Old", "Old desc", 1L, new Date(System.currentTimeMillis() + 1000000), "old.png"));
        assignment.updateInformation("New", "New desc", 2L, new Date(System.currentTimeMillis() + 2000000), "new.png");

        assertThat(assignment.getTitle()).isEqualTo("New");
        assertThat(assignment.getCourseId()).isEqualTo(2L);
        assertThat(assignment.getDescription()).isEqualTo("New desc");
        assertThat(assignment.getImageUrl()).isEqualTo("new.png");
    }

    // Verifica que los métodos addFileUrl y removeFileUrl
    // modifican correctamente la lista de archivos del Assignment.
    @Test
    void addAndRemoveFileUrl_shouldModifyFileList() {
        Assignment assignment = new Assignment(new CreateAssignmentCommand(
                "Test", "Desc", 1L, new Date(System.currentTimeMillis() + 1000000), "img.png"));
        assignment.addFileUrl("file1.pdf");
        assignment.addFileUrl("file2.pdf");

        assertThat(assignment.getFileUrls()).containsExactly("file1.pdf", "file2.pdf");

        assignment.removeFileUrl("file1.pdf");
        assertThat(assignment.getFileUrls()).containsExactly("file2.pdf");
    }

    // Verifica que al intentar crear un Assignment con descripción nula,
    // se lanza IllegalArgumentException.
    @Test
    void createAssignment_withNullTitle_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new CreateAssignmentCommand(null, "desc", 1L, new Date(System.currentTimeMillis() + 1000000), "img.png"));
    }

    // Verifica que al intentar crear un Description con descripción nula,
    // se lanza IllegalArgumentException.
    @Test
    void createAssignment_withNullDescription_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new CreateAssignmentCommand("Tarea 1", null, 1L, new Date(System.currentTimeMillis() + 1000000), "img.png"));
    }
}
