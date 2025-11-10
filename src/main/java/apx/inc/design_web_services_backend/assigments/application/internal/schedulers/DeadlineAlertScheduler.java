package apx.inc.design_web_services_backend.assigments.application.internal.schedulers;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.events.AssignmentDeadlineCloseAlertEvent;
import apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeadlineAlertScheduler {

    private final AssignmentRepository assignmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 8,12,16,20 * * *") // 8AM, 12PM, 4PM, 8PM
    public void checkApproachingDeadlines() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusHours(24); // 24 horas desde ahora

        List<Assignment> assignments = assignmentRepository.findByDeadlineBetween(
                convertToDate(now),
                convertToDate(threshold)
        );

        assignments.forEach(assignment -> {
            long hoursRemaining = calculateHoursRemaining(assignment.getDeadline());

            eventPublisher.publishEvent(new AssignmentDeadlineCloseAlertEvent(
                    this,
                    assignment.getId(),
                    assignment.getTitle(),
                    assignment.getCourseId(),
                    assignment.getDeadline(),
                    hoursRemaining
            ));


        });


    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private long calculateHoursRemaining(Date deadline) {
        long diff = deadline.getTime() - System.currentTimeMillis();
        return diff / (1000 * 60 * 60); // Convertir a horas
    }
}
