package apx.inc.design_web_services_backend.notifications.infrastructure.persistence.jpa.repositories;

import apx.inc.design_web_services_backend.notifications.domain.model.aggregates.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);

    List<Notification> findBySourceAssignmentIdAndReadFalse(Long assignmentId);
}
