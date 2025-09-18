package apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    boolean existsByTitle(String title);

    List<Assignment> findByCourseId(Long courseId);

    boolean existsByTitleAndCourseId(String title, Long courseId);

    Optional<Assignment> findByTitleAndCourseId(String title, Long courseId);
}
