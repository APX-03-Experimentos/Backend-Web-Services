package apx.inc.design_web_services_backend.iam.rest.resources;

import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import apx.inc.design_web_services_backend.shared.interfaces.rest.resources.CourseResource;

import java.util.List;

public record UserResource(
        Long id,
        String userName,
        List<Roles> roles,
        List<CourseResource> courseResources  // ← Lista de cursos del usuario
) { }
