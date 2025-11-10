package apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserNameAndPassword(String userName,String password);

    // Additional query methods can be defined here if needed


    Optional<User> findByUserName(String userName);

    Boolean existsByUserName(String userName);

    //List<ProfileInGroup> findProfilesInGroupsByIdAndProfilesInGroups(Long userId, Long groupId);

    // En lugar de buscar por ID y luego acceder a las relaciones lazy
    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.studentInCourses sc " +
            "LEFT JOIN FETCH u.userRoles r " +
            "WHERE sc.id = :courseId")
    List<User> findStudentsByCourseIdWithCoursesAndRoles(@Param("courseId") Long courseId);
}
