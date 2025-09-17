package apx.inc.design_web_services_backend.courses.infrastructure.persistence.jpa.repositories;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
