package apx.inc.design_web_services_backend.courses.domain.model.commands;

import java.util.Date;

public record SetJoinCodeCommand(
        Long courseId,
        String keycode,
        Date expiration
) {

}
