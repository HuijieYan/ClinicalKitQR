package ClinicalKitQR;

import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.models.Manufacturer;
import ClinicalKitQR.equipment.models.SentEquipment;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.equipment.services.ManufacturerService;
import ClinicalKitQR.issue.models.Issue;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.TrustService;
import ClinicalKitQR.login.services.UserGroupService;
import ClinicalKitQR.mail.models.Mail;
import ClinicalKitQR.mail.services.MailService;
import ClinicalKitQR.viewing.models.Viewing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Configuration
@Profile("sample")
public class SampleConfig {
    @Bean
    CommandLineRunner sampleCommandLineRunner(ManufacturerService manufacturerService, MailService mailService, EquipmentService equipmentService, UserGroupService userGroupService, TrustService trustService){
        return args -> {
            if (userGroupService.getAll().size()>1){
                return;
            }

            String sampleData = "{\"Announcement\":\"<p>Updated description</p>\",\"Description\":\"<p><span style=\\\"font-size: 10pt;\\\">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Augue interdum velit euismod in pellentesque massa. Sollicitudin ac orci phasellus egestas. Vulputate eu scelerisque felis imperdiet proin fermentum. Eget mi proin sed libero enim sed faucibus turpis. Faucibus vitae aliquet nec ullamcorper sit amet. Faucibus in ornare quam viverra orci sagittis eu volutpat odio. Nec feugiat nisl pretium fusce. Erat nam at lectus urna duis. Pellentesque diam volutpat commodo sed. Erat imperdiet sed euismod nisi porta lorem mollis aliquam ut. Sem nulla pharetra diam sit amet nisl suscipit adipiscing. Eget sit amet tellus cras adipiscing enim eu turpis. Donec ac odio tempor orci dapibus.</span></p>\\n<p><iframe src=\\\"https://www.youtube.com/embed/7Y3isrH2b8A\\\" width=\\\"300\\\" height=\\\"168.21428571428572\\\" allowfullscreen=\\\"allowfullscreen\\\"></iframe></p>\\n<p><span style=\\\"font-size: 10pt;\\\">Donec adipiscing tristique risus nec feugiat in fermentum posuere. Fames ac turpis egestas integer eget. Duis tristique sollicitudin nibh sit amet commodo nulla facilisi. Fames ac turpis egestas integer eget aliquet nibh praesent. Amet luctus venenatis lectus magna. Pulvinar etiam non quam lacus suspendisse faucibus interdum. Erat imperdiet sed euismod nisi porta lorem. Est ante in nibh mauris cursus. In arcu cursus euismod quis viverra nibh cras pulvinar mattis. Mauris a diam maecenas sed enim ut. Metus vulputate eu scelerisque felis imperdiet proin fermentum leo. Arcu cursus vitae congue mauris rhoncus aenean vel. Diam maecenas ultricies mi eget mauris.</span></p>\\n<p><span style=\\\"font-size: 10pt;\\\"><a href=\\\"https://www.draeger.com/en_uk/Products/Babylog-VN800\\\">Babylog-VN800</a></span></p>\\n<p><span style=\\\"font-size: 10pt;\\\">Erat velit scelerisque in dictum non consectetur a. Arcu vitae elementum curabitur vitae nunc sed velit dignissim sodales. Ornare arcu dui vivamus arcu felis bibendum ut tristique et. Eu augue ut lectus arcu bibendum at varius vel pharetra. Ornare arcu odio ut sem nulla pharetra diam sit. Vitae elementum curabitur vitae nunc. Ultricies mi quis hendrerit dolor magna eget est. Vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt. Lorem ipsum dolor sit amet. Phasellus vestibulum lorem sed risus ultricies tristique. Magna ac placerat vestibulum lectus mauris ultrices eros. Non odio euismod lacinia at. Congue mauris rhoncus aenean vel elit scelerisque mauris pellentesque pulvinar. Purus in mollis nunc sed id semper risus in. Viverra accumsan in nisl nisi scelerisque eu ultrices. Montes nascetur ridiculus mus mauris vitae ultricies leo integer. Quis commodo odio aenean sed adipiscing. Lacinia at quis risus sed vulputate. Nisl pretium fusce id velit ut tortor pretium.</span></p>\",\"Description2\":\"<p><span style=\\\"font-size: 10pt;\\\">Adipiscing bibendum est ultricies integer quis auctor elit sed. Integer quis auctor elit sed vulputate mi. Vel fringilla est ullamcorper eget nulla. Fames ac turpis egestas maecenas pharetra. Scelerisque eleifend donec pretium vulputate sapien nec. At urna condimentum mattis pellentesque id nibh. Nibh tortor id aliquet lectus proin. Aliquam id diam maecenas ultricies. Tristique sollicitudin nibh sit amet commodo nulla. Lacinia quis vel eros donec ac odio tempor. Imperdiet proin fermentum leo vel orci porta. Augue interdum velit euismod in. Faucibus turpis in eu mi bibendum neque. Lorem ipsum dolor sit amet consectetur adipiscing elit pellentesque habitant. Ut tellus elementum sagittis vitae et leo duis ut. Nibh mauris cursus mattis molestie a iaculis. Adipiscing tristique risus nec feugiat. Vel pharetra vel turpis nunc eget.</span></p>\\n<p><span style=\\\"font-size: 10pt;\\\"><img src=\\\"https://www.draeger.com/Products/Media/Draeger-Babylog-VN800-D-5755-2018.jpg?imwidth=1024\\\" alt=\\\"\\\" width=\\\"324\\\" height=\\\"216\\\" /></span></p>\\n<p><span style=\\\"font-size: 10pt;\\\">Vestibulum porta condimentum est quis fringilla. Sed venenatis at diam in convallis. Phasellus dignissim faucibus lorem. Fusce ac arcu felis. Phasellus feugiat ante ac enim hendrerit, id porttitor mauris feugiat. Proin vel vestibulum ligula, tristique feugiat augue. Proin ullamcorper finibus sapien. Sed facilisis, quam non posuere maximus, purus leo pulvinar diam, in finibus felis mauris eu augue. Cras dictum metus eu nulla efficitur, id mattis nulla maximus. Fusce mollis molestie nisl, nec pulvinar nulla fringilla eget. Quisque eleifend orci velit, vel ullamcorper mauris placerat a. Aliquam fringilla magna dui, eu sagittis purus fermentum sed. Nulla at odio pharetra, condimentum dolor vitae, mollis risus. Phasellus sodales, enim nec commodo fringilla, libero ex molestie velit, vitae euismod leo urna et est.</span></p>\\n<p><span style=\\\"font-size: 10pt;\\\">Pellentesque mattis vestibulum erat. Ut justo eros, sollicitudin non justo ac, vehicula auctor elit. Duis vitae congue dui. Ut id orci a est iaculis fermentum. Etiam eu consequat ex, non eleifend felis. Pellentesque gravida, nisl pretium sodales gravida, nisl ex sodales dolor, ac dictum nulla nibh tempor massa. Vivamus eget interdum lectus. Phasellus accumsan lectus scelerisque placerat dapibus. Suspendisse viverra luctus sem non imperdiet. Sed varius facilisis ipsum, id posuere elit condimentum nec. Fusce neque ex, sollicitudin id ornare at, vehicula at urna.</span></p>\"}";

            Trust trust = new Trust("Sample Trust");
            Hospital trustAdmin = trust.getHospitals().get(0);
            Hospital hospital = new Hospital("MyHospital",trust);
            Hospital hospital2 = new Hospital("MyHospital2",trust);
            trust.addHospital(hospital);
            trust.addHospital(hospital2);
            UserGroup group = new UserGroup("group1","g1","123",hospital,true,"g1@nhs.com","admin");
            UserGroup group2 = new UserGroup("group2","g2","123",hospital,true);
            UserGroup group3 = new UserGroup("group3","g3","123",hospital,false);
            UserGroup g3 = new UserGroup("admin1","admin","123",trustAdmin,true,"trustAdmin@nhs.com","admin");
            UserGroup g4 = new UserGroup("group1","g1","123",hospital2,true);
            UserGroup g5 = new UserGroup("group5","g5","123",hospital,false);
            UserGroup trust2Group3 = new UserGroup("group3","g3","123",hospital2,false);

            Manufacturer apple = new Manufacturer("Apple");
            Manufacturer banana = new Manufacturer("Banana");
            manufacturerService.save(apple);
            manufacturerService.save(banana);
            Equipment equipment1 = new Equipment("Equipment1",sampleData,hospital,"Respiratory","Neonatal",new EquipmentModel("B1",banana));
            Equipment equipment2 = new Equipment("Equipment2",sampleData,hospital,"Gastrointestinal","Adult",new EquipmentModel("A1",apple));
            Equipment equipment3 = new Equipment("Equipment3",sampleData,hospital2,"Metabolic","Children",new EquipmentModel("B2",banana));
            Equipment equipment4 = new Equipment("Equipment4",sampleData,hospital2,"Neurological","Adult",new EquipmentModel("A2",apple));
            trustAdmin.addGroup(g3);
            hospital.addGroup(group);
            hospital.addGroup(group2);
            hospital.addGroup(group3);
            hospital.addGroup(g5);
            hospital2.addGroup(g4);
            hospital2.addGroup(trust2Group3);
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

            trustService.save(trust);

            Mail mail = new Mail(group.getHospitalId().getHospitalId(), group.getUsername(), LocalDateTime.now(ZoneOffset.UTC),"Title","description",g3);
            mail.addEquipment(new SentEquipment(equipment1));
            mail.addEquipment(new SentEquipment(equipment3));
            mailService.save(mail);
            Mail mailCopy = new Mail(group.getHospitalId().getHospitalId(), group.getUsername(), LocalDateTime.now(ZoneOffset.UTC),"Title","description",null);
            mailCopy.addEquipment(new SentEquipment(equipment1));
            mailCopy.addEquipment(new SentEquipment(equipment3));
            mailService.save(mailCopy);

            Mail mail2 = new Mail(g3.getHospitalId().getHospitalId(), g3.getUsername(), LocalDateTime.now(ZoneOffset.UTC),"Title2","description",group);
            mail2.addEquipment(new SentEquipment(equipment1));
            mail2.addEquipment(new SentEquipment(equipment2));
            mailService.save(mail2);
            mailCopy = new Mail(g3.getHospitalId().getHospitalId(), g3.getUsername(), LocalDateTime.now(ZoneOffset.UTC),"Title2","description",null);
            mailCopy.addEquipment(new SentEquipment(equipment1));
            mailCopy.addEquipment(new SentEquipment(equipment2));
            mailService.save(mailCopy);

            Mail mail3 = new Mail(group2.getHospitalId().getHospitalId(), group2.getUsername(), LocalDateTime.now(ZoneOffset.UTC),"Title3","description",g3);
            mail3.addEquipment(new SentEquipment(equipment3));
            mailService.save(mail3);
            mailCopy = new Mail(group2.getHospitalId().getHospitalId(), group2.getUsername(), LocalDateTime.now(ZoneOffset.UTC),"Title3","description",null);
            mailCopy.addEquipment(new SentEquipment(equipment3));
            mailService.save(mailCopy);

            trust = new Trust("Sample Trust 2");
            hospital = new Hospital("New Hospital",trust);
            trust.addHospital(hospital);
            group = new UserGroup("Admin A","admin","123",hospital,true,"adminA@nhs.com");
            hospital.addGroup(group);
            group = new UserGroup("Trust Admin","admin","123",trust.getHospitals().get(0), true,"adminA@nhs.com");
            hospital.addGroup(group);
            Manufacturer manufacturer = new Manufacturer("Cat");
            manufacturerService.save(manufacturer);
            Equipment equipment = new Equipment("Equipment3",sampleData,hospital,"Haematological","Neonatal",new EquipmentModel("C1",manufacturer));
            trustService.save(trust);
            equipmentService.save(equipment);
        };

    }
}
