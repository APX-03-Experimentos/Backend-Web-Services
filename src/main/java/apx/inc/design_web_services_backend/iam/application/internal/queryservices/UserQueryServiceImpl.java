package apx.inc.design_web_services_backend.iam.application.internal.queryservices;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.domain.model.queries.*;
//import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.ProfileInGroup;
import apx.inc.design_web_services_backend.iam.domain.services.UserQueryService;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery getUserByIdQuery) {
        return userRepository.findById(getUserByIdQuery.userId());
    }

    @Override
    public Optional<User> handle(GetUserByUsernameQuery getUserByUsernameQuery) {
        return userRepository.findByUsername(getUserByUsernameQuery.username());
    }

    @Override
    public List<User> handle(GetAllUsersQuery getAllUsersQuery) {
        return userRepository.findAll();
    }

//    @Override
//    public Optional<User> handle(GetUserByEmailAndPasswordQuery getUserByEmailAndPasswordQuery) {
//        return userRepository.findByEmailAndPassword(getUserByEmailAndPasswordQuery.email(), getUserByEmailAndPasswordQuery.password());
//    }

//    @Override
//    public Optional<User> handle(GetUserByEmailQuery getUserByEmailQuery) {
//        return userRepository.findByEmail(getUserByEmailQuery.email());
//    }

//    @Override
//    public Optional<ProfileInGroup> handle(GetProfilesInGroupsByGroupIdAndStudentIdQuery query) {
//        return userRepository.findById(query.studentId())
//                .map(User::getProfilesInGroups)
//                .orElse(List.of())
//                .stream()
//                .filter(p -> p.getGroupId().equals(query.groupId()))
//                .findFirst(); // ✅ Solo el primero, como Optional
//    }

    @Override
    public List<User> handle(GetUsersByCourseIdQuery query) {
        return userRepository.findAll().stream()
                .filter(user -> user.getStudentInCourses().stream()
                        .anyMatch(course -> course.getId().equals(query.courseId())))
                .toList();
    }

//    @Override
//    public Optional<String> handle(GetFullNameByIdQuery getFullNameByIdQuery) {
//        return userRepository.findById(getFullNameByIdQuery.userId())
//                .map(user -> user.getFirstName() + " " + user.getLastName());
//    }
    @Override
    @Transactional(readOnly = true)
    public List<User> handle(GetStudentsByCourseIdQuery query) {
        // USAR EL NUEVO MÉTODO DEL REPOSITORY que hace el JOIN FETCH
        return userRepository.findStudentsByCourseIdWithCoursesAndRoles(query.courseId());
    }
}
