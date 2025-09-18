package apx.inc.design_web_services_backend.assigments.domain.services;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.GetAllAssignmentsQuery;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.GetAssignmentByIdQuery;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.GetAssignmentsByGroupIdQuery;

import java.util.List;
import java.util.Optional;

public interface AssignmentQueryService {

    Optional<Assignment> handle(GetAssignmentByIdQuery getAssignmentByIdQuery, Long userId);

    List<Assignment> handle(GetAllAssignmentsQuery getAllAssignmentsQuery);

    List<Assignment> handle(GetAssignmentsByGroupIdQuery getAssignmentsByGroupIdQuery, Long userId);
}
