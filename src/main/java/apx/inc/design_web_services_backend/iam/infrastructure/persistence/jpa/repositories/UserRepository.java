package apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserNameAndPassword(String userName,String password);

    // Additional query methods can be defined here if needed


    Optional<User> findByUserName(String userName);

    Boolean existsByUserName(String userName);

    //List<ProfileInGroup> findProfilesInGroupsByIdAndProfilesInGroups(Long userId, Long groupId);
}
