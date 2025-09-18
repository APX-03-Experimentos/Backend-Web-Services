package apx.inc.design_web_services_backend.assigments.domain.services;


import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Submission;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface SubmissionQueryService {

    Optional<Submission> handle(GetSubmissionByIdQuery getSubmissionByIdQuery, Long userId);

    List<Submission> handle(GetAllSubmissionsQuery query);

    List<Submission> handle(GetSubmissionsByAssignmentIdQuery query);

    List<Submission> handle(GetSubmissionsByStudentIdQuery query);

    List<Submission> handle(GetSubmissionsByStudentIdAndAssignmentIdQuery query);

    List<Submission> handle(GetSubmissionsByStudentIdAndCourseIdQuery query);

    List<Submission> handle(GetSubmissionsByCourseIdQuery query);
}
