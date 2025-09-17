package apx.inc.design_web_services_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DesignWebServicesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesignWebServicesBackendApplication.class, args);
    }

}
