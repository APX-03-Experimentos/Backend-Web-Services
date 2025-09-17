package apx.inc.design_web_services_backend.iam.rest.resources;

import java.util.Date;

public record CourseJoinCodeResource(
        String key,
        Date expiration
) {
}
