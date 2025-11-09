package apx.inc.design_web_services_backend.iam.application.acl;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.domain.model.queries.GetUserByIdQuery;
import apx.inc.design_web_services_backend.iam.domain.model.queries.GetUsersByCourseIdQuery;
import apx.inc.design_web_services_backend.iam.domain.services.UserQueryService;
import apx.inc.design_web_services_backend.iam.interfaces.acl.IamContextFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IamContextFacadeImpl implements IamContextFacade {

    private final UserQueryService userQueryService;

    @Override
    public Optional<User> fetchUserById(Long userId) {
        return userQueryService.handle(new GetUserByIdQuery(userId));

    }

    @Override
    public List<Long> getStudentsByCourseId(Long courseId) {
        List<User> students = userQueryService.handle(new GetUsersByCourseIdQuery(courseId));
        return students.stream()
                .map(User::getId)
                .toList();
    }
}
