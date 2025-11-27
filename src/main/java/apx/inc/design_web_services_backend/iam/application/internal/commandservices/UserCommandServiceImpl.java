package apx.inc.design_web_services_backend.iam.application.internal.commandservices;

import apx.inc.design_web_services_backend.iam.application.internal.outboundservices.hashing.HashingService;
import apx.inc.design_web_services_backend.iam.application.internal.outboundservices.tokens.TokenService;
import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.domain.model.commands.*;
import apx.inc.design_web_services_backend.iam.domain.services.UserCommandService;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RecaptchaService recaptchaService;



    @Override
    public Optional<User> handle(UpdateUserCommand updateUserCommand, Long userId) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        //Check if a user with the same email already exists
        var existingUserWithEmail = userRepository.findByUsername(updateUserCommand.username());
        if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(userId)) {
            throw new IllegalArgumentException("User with username " + updateUserCommand.username() + " already exists");
        }

        var userToUpdate = userOptional.get();

        // ✅ Aquí cifras la contraseña SOLO si no viene vacía
        String encodedPassword = updateUserCommand.password();
        if (encodedPassword != null && !encodedPassword.isBlank()) {
            encodedPassword = hashingService.encode(encodedPassword);
        } else {
            // Si viene vacía, mantén la actual
            encodedPassword = userToUpdate.getPassword();
        }

        // ✅ Crea un nuevo comando con la contraseña cifrada
        var commandWithEncodedPassword = new UpdateUserCommand(
                updateUserCommand.username(),
                encodedPassword,
                updateUserCommand.roles()
        );

        try{
            var updatedUser= userRepository.save(userToUpdate.updateUserDetails(commandWithEncodedPassword));
            return Optional.of(updatedUser);
        }catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            return Optional.empty();
        }
    }

    @Override
    public void handle(DeleteUserCommand deleteUserCommand) {
        if (!userRepository.existsById(deleteUserCommand.userId())) {
            throw new IllegalArgumentException("User with ID " + deleteUserCommand.userId() + " not found");
        }

        try{
            userRepository.deleteById(deleteUserCommand.userId());
        } catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> handle(LeaveCourseCommand leaveCourseCommand) {
        // Check if the user exists
        var userOptional = userRepository.findById(leaveCourseCommand.userId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + leaveCourseCommand.userId() + " not found");
        }


        var user = userOptional.get();

        // ✅ Validar que pertenece al curso (CORREGIDO)
        boolean belongsToCourse = user.getStudentInCourses().stream()
                .anyMatch(course -> course.getId().equals(leaveCourseCommand.courseId()));

        if (!belongsToCourse) {
            throw new IllegalArgumentException("User does not belong to the group with ID " + leaveCourseCommand.courseId());
        }
        user.removeFromCourse(leaveCourseCommand.courseId());

        // Save the updated user
        try {
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            throw new RuntimeException("Error while removing user from group", e);
        }

    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand signInCommand) {
        var user = userRepository.findByUsername(signInCommand.username());

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with user name " + signInCommand.username() + " not found");
        }
        if (!hashingService.matches(signInCommand.password(), user.get().getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        var token = tokenService.generateToken(user.get().getUsername());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    @Override
    public Optional<User> handle(SignUpCommand signUpCommand) {
        boolean isCaptchaValid = recaptchaService.verifyRecaptcha(signUpCommand.recaptchaToken());

        if (!isCaptchaValid) {
            throw new IllegalArgumentException("CAPTCHA verification failed");
        }

        if (userRepository.existsByUsername(signUpCommand.username())) {
            throw new IllegalArgumentException("User with user name " + signUpCommand.username() + " already exists");
        }
        var roles= signUpCommand.roles().stream().map(
                role->roleRepository.findByName(role)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + role + " not found"))
                ).toList();
        var user = new User(signUpCommand.username(), hashingService.encode(signUpCommand.password()), roles);
        userRepository.save(user);
        return userRepository.findByUsername(signUpCommand.username());
    }
}
