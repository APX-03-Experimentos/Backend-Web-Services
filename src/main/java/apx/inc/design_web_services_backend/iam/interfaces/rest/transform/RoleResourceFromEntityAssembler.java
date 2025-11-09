package apx.inc.design_web_services_backend.iam.interfaces.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role roleEntity) {

        return new RoleResource(
                roleEntity.getId(),
                roleEntity.getStringName()
        );
    }
}
