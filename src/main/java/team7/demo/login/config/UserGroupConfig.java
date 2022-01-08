package team7.demo.login.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team7.demo.equipment.models.Equipment;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.Trust;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.HospitalService;

@Configuration
public class UserGroupConfig {
    @Bean
    CommandLineRunner commandLineRunner(HospitalService service){
        return args -> {
            Trust trust = new Trust("Team7");
            Hospital trustAdmin = trust.getHospitals().get(0);
            Hospital hospital = new Hospital("MyHospital",trust);
            UserGroup group = new UserGroup("group1","g1","123",true);
            UserGroup group2 = new UserGroup("group2","g2","123",false);
            UserGroup g3 = new UserGroup("admin1","admin","123",false);
            trustAdmin.addGroup(g3);
            Equipment equipment1 = new Equipment("Equipment1","Some Description");
            Equipment equipment2 = new Equipment("Equipment2","Some Description");
            hospital.addGroup(group);
            hospital.addGroup(group2);
            hospital.addEquipment(equipment1);
            hospital.addEquipment(equipment2);
            service.save(hospital);
        };
    }
}
