package apx.inc.design_web_services_backend.iam.application.internal.commandservices;

import apx.inc.design_web_services_backend.iam.domain.model.commands.SeedRolesCommand;
import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import apx.inc.design_web_services_backend.iam.domain.services.RoleCommandService;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public void handle(SeedRolesCommand seedRolesCommand) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(Roles.valueOf(role.name())));
            }
        });
    }
}
