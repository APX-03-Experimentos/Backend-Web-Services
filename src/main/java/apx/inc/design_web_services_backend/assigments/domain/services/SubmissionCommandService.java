package apx.inc.design_web_services_backend.assigments.domain.services;


import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Submission;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.DeleteSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.GradeSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.UpdateSubmissionCommand;

import java.util.Optional;

public interface SubmissionCommandService {


    Long handle(CreateSubmissionCommand createSubmissionCommand);
    //Porque se espera que, después de crear el submission, se devuelva su ID generado

    Optional<Submission> handle(UpdateSubmissionCommand updateSubmissionCommand);
    //¿Por qué retorna Optional<Course>? Porque puede que el curso no exista.
    //Si lo encuentra y actualiza → devuelve el Course.
    //Si no lo encuentra → devuelve Optional.empty().

    void handle(DeleteSubmissionCommand deleteSubmissionCommand);
    //¿Por qué retorna void? Porque no se necesita retornar nada.

    Optional<Submission> handle(GradeSubmissionCommand command);
}
