package apx.inc.design_web_services_backend.iam.domain.services;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    List<User> handle(GetAllUsersQuery getAllUsersQuery);

    Optional<User> handle(GetUserByIdQuery getUserByIdQuery);

    Optional<User> handle(GetUserByUserNameQuery getUserByUserNameQuery);

    List<User> handle(GetUsersByCourseIdQuery getUsersByCourseIdQuery);

    List<User> handle(GetStudentsByCourseIdQuery query);
}
