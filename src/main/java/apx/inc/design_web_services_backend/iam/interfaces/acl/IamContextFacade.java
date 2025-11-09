package apx.inc.design_web_services_backend.iam.interfaces.acl;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;

import java.util.List;
import java.util.Optional;

public interface IamContextFacade {

    Optional<User> fetchUserById(Long userId);

    List<Long> getStudentsByCourseId(Long courseId);

}
