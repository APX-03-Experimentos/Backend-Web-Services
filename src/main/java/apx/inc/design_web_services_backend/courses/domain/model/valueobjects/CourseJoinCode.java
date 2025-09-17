package apx.inc.design_web_services_backend.courses.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.Date;

@Embeddable
public record CourseJoinCode(String key, Date expiration) {
    public CourseJoinCode {

        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
    }
}
