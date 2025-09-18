package apx.inc.design_web_services_backend.courses.infrastructure.persistence.jpa.repositories;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.courses.domain.model.valueobjects.CourseJoinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCourseJoinCode(CourseJoinCode courseJoinCode);

    List<Course> findByTeacherId(Long teacherId);
}
