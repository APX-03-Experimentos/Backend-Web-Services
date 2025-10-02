package apx.inc.design_web_services_backend.assigments.domain.services;


import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.*;

import java.util.Optional;

public interface AssignmentCommandService {

    Long handle(CreateAssignmentCommand createAssignmentCommand, Long userid);

    Optional<Assignment> handle(UpdateAssignmentCommand updateAssignmentCommand, Long userid);

    void handle(DeleteAssignmentCommand deleteAssignmentCommand);


    void handle(AddFilesToAssignmentCommand addFilesToAssignmentCommand);  // Para 1 o múltiples archivos
    void handle(RemoveFileFromAssignmentCommand removeFileFromAssignmentCommand); // Para eliminar 1 archivo

}
