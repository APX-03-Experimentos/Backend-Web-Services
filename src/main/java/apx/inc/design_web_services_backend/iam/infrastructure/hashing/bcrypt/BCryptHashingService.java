package apx.inc.design_web_services_backend.iam.infrastructure.hashing.bcrypt;

import apx.inc.design_web_services_backend.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {




}
