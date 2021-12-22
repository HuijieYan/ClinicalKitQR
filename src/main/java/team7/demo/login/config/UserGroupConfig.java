package team7.demo.login.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.Trust;
import team7.demo.login.models.UserGroup;
import team7.demo.login.repositories.HospitalRepository;
import team7.demo.login.repositories.TrustRepository;

@Configuration
public class UserGroupConfig {
    @Bean
    CommandLineRunner commandLineRunner(HospitalRepository repo){
        return args -> {
            Trust trust = new Trust("Team7");
            Hospital hospital = new Hospital("MyHospital",trust);
            UserGroup group = new UserGroup("group1","g1","123",hospital);
            UserGroup group2 = new UserGroup("group2","g2","123",hospital);
            repo.save(hospital);
        };
    }
}
