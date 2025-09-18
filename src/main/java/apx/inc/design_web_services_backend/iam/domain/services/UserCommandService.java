package apx.inc.design_web_services_backend.iam.domain.services;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.domain.model.commands.*;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

public interface UserCommandService {
    //Optional<User> handle(CreateUserCommand createUserCommand);

    void handle(DeleteUserCommand deleteUserCommand);

    Optional<User> handle(UpdateUserCommand updateUserCommand , Long userId);

    Optional<ImmutablePair<User,String>> handle(SignInCommand signInCommand);

    Optional<User> handle(SignUpCommand signUpCommand);

    Optional<User> handle(LeaveCourseCommand leaveCourseCommand);
}
