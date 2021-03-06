package ClinicalKitQR.login.models.config;

import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.TrustService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UserGroupConfig {
    @Bean
    CommandLineRunner commandLineRunner(TrustService service) {
        return args -> {
            if(service.getAll().size()==0){
                Trust trust = new Trust("Team 7");
                UserGroup admin = new UserGroup("admin1","admin","123",trust.getHospitals().get(0),true,"trustAdmin@nhs.com","admin");
                trust.getHospitals().get(0).addGroup(admin);
                service.save(trust);
            }
        };
    }
}
