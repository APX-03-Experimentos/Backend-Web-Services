package apx.inc.design_web_services_backend.iam.domain.services;

import apx.inc.design_web_services_backend.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {

    void handle(SeedRolesCommand seedRolesCommand);

}
