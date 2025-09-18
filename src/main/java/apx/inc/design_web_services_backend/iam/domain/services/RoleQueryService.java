package apx.inc.design_web_services_backend.iam.domain.services;

import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.iam.domain.model.queries.GetAllRolesQuery;

import java.util.List;

public interface RoleQueryService {

    List<Role> handle(GetAllRolesQuery getAllRolesQuery);
}
