package team7.demo.login.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team7.demo.equipment.models.Equipment;
import team7.demo.issue.models.Issue;
import team7.demo.issue.services.IssueService;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.Trust;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.HospitalService;

import java.time.LocalDate;

@Configuration
public class UserGroupConfig {
    @Bean
    CommandLineRunner commandLineRunner(HospitalService service, IssueService issueService){
        return args -> {
            Trust trust = new Trust("Team7");
            Hospital trustAdmin = trust.getHospitals().get(0);
            Hospital hospital = new Hospital("MyHospital",trust);
            Hospital hospital2 = new Hospital("MyHospital2",trust);
            trust.addHospital(hospital2);
            UserGroup group = new UserGroup("group1","g1","123",hospital,true);
            UserGroup group2 = new UserGroup("group2","g2","123",hospital,false);
            UserGroup g3 = new UserGroup("admin1","admin","123",trustAdmin,false);
            UserGroup g4 = new UserGroup("group1","g1","123",hospital2,false);

            Equipment equipment1 = new Equipment("Equipment1","Some Description",hospital);
            Equipment equipment2 = new Equipment("Equipment2","Some Description",hospital);
            Equipment equipment3 = new Equipment("Equipment3","Some Description",hospital2);
            trustAdmin.addGroup(g3);
            hospital.addGroup(group);
            hospital.addGroup(group2);
            hospital2.addGroup(g4);
            hospital.addEquipment(equipment1);
            hospital.addEquipment(equipment2);
            hospital2.addEquipment(equipment3);
            Issue issue = new Issue(LocalDate.now(),group,equipment1,"Some Problem Description");
            equipment1.addIssue(issue);
            group.addIssue(issue);
            service.save(hospital);
            service.save(hospital2);
        };
    }
}
