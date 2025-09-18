package apx.inc.design_web_services_backend.iam.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.shared.interfaces.rest.resources.CourseJoinCodeResource;
import apx.inc.design_web_services_backend.shared.interfaces.rest.resources.CourseResource;
import apx.inc.design_web_services_backend.iam.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(
                user.getId(),
                user.getUserName(),
                user.getUserRoles().stream().map(Role::getName).toList(),
                user.getStudentInCourses().stream().map(
                        course -> new CourseResource(
                                course.getId(),
                                course.getTeacherId(),
                                course.getTitle(),
                                course.getImageUrl(),
                                course.getCourseJoinCode().key()
                        )).toList()
        );
    }

}
