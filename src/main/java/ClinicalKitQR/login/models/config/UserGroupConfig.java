package ClinicalKitQR.login.models.config;

import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.models.Manufacturer;
import ClinicalKitQR.issue.models.Issue;
import ClinicalKitQR.mail.models.Mail;
import ClinicalKitQR.mail.services.MailService;
import ClinicalKitQR.viewing.models.Viewing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ClinicalKitQR.equipment.models.SentEquipment;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.HospitalService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class UserGroupConfig {
    @Bean
    CommandLineRunner commandLineRunner(HospitalService service, MailService mailService){
        return args -> {
            Trust trust = new Trust("Team7");
            Hospital trustAdmin = trust.getHospitals().get(0);
            Hospital hospital = new Hospital("MyHospital",trust);
            Hospital hospital2 = new Hospital("MyHospital2",trust);
            trust.addHospital(hospital2);
            UserGroup group = new UserGroup("group1","g1","123",hospital,true,"g1@nhs.com","admin");
            UserGroup group2 = new UserGroup("group2","g2","123",hospital,true);
            UserGroup group3 = new UserGroup("group3","g3","123",hospital,false);
            UserGroup g3 = new UserGroup("admin1","admin","123",trustAdmin,true,"trustAdmin@nhs.com","admin");
            UserGroup g4 = new UserGroup("group1","g1","123",hospital2,false);
            UserGroup g5 = new UserGroup("group5","g5","123",hospital,false);

            Manufacturer apple = new Manufacturer("Apple");
            Manufacturer banana = new Manufacturer("Banana");
            Equipment equipment1 = new Equipment("Equipment1","Some Description",hospital,"A","Neonatal",new EquipmentModel("B1",banana));
            Equipment equipment2 = new Equipment("Equipment2","Some Description",hospital,"C","Adult",new EquipmentModel("A1",apple));
            Equipment equipment3 = new Equipment("Equipment3","Some Description",hospital2,"B","Children",new EquipmentModel("B2",banana));
            Equipment equipment4 = new Equipment("Equipment4","Some Description",hospital2,"A","Adult",new EquipmentModel("A2",apple));
            trustAdmin.addGroup(g3);
            hospital.addGroup(group);
            hospital.addGroup(group2);
            hospital.addGroup(group3);
            hospital.addGroup(g5);
            hospital2.addGroup(g4);
            hospital.addEquipment(equipment1);
            hospital.addEquipment(equipment2);
            hospital2.addEquipment(equipment3);
            hospital2.addEquipment(equipment4);
            Issue issue = new Issue(LocalDate.now(),group,equipment1,"Some Problem Description");
            equipment1.addIssue(issue);
            group.addIssue(issue);

            // example code for viewings
            Viewing viewing =  new Viewing(equipment1, LocalDate.now(),group3);
            equipment1.addViewing(viewing);
            group.addViewing(viewing);

            service.save(hospital);
            service.save(hospital2);

            Mail mail = new Mail(group.getHospitalId().getHospitalId(), group.getUsername(), LocalDateTime.now(),"Title","description",g3);
            mail.addEquipment(new SentEquipment(equipment1));
            mail.addEquipment(new SentEquipment(equipment3));
            mailService.save(mail);
            Mail mailCopy = new Mail(group.getHospitalId().getHospitalId(), group.getUsername(), LocalDateTime.now(),"Title","description",null);
            mailCopy.addEquipment(new SentEquipment(equipment1));
            mailCopy.addEquipment(new SentEquipment(equipment3));
            mailService.save(mailCopy);

            Mail mail2 = new Mail(g3.getHospitalId().getHospitalId(), g3.getUsername(), LocalDateTime.now(),"Title2","description",group);
            mail2.addEquipment(new SentEquipment(equipment1));
            mail2.addEquipment(new SentEquipment(equipment2));
            mailService.save(mail2);
            mailCopy = new Mail(g3.getHospitalId().getHospitalId(), g3.getUsername(), LocalDateTime.now(),"Title2","description",null);
            mailCopy.addEquipment(new SentEquipment(equipment1));
            mailCopy.addEquipment(new SentEquipment(equipment2));
            mailService.save(mailCopy);

            Mail mail3 = new Mail(group2.getHospitalId().getHospitalId(), group2.getUsername(), LocalDateTime.now(),"Title3","description",g3);
            mail3.addEquipment(new SentEquipment(equipment3));
            mailService.save(mail3);
            mailCopy = new Mail(group2.getHospitalId().getHospitalId(), group2.getUsername(), LocalDateTime.now(),"Title3","description",null);
            mailCopy.addEquipment(new SentEquipment(equipment3));
            mailService.save(mailCopy);

        };
    }
}
