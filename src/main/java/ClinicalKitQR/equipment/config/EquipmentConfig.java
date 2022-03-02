package ClinicalKitQR.equipment.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;

import java.awt.image.BufferedImage;

@Configuration
public class EquipmentConfig {
    @Bean
    CommandLineRunner equipmentRunner(EquipmentService service){
        return args -> {
            Hospital hospital = new Hospital("New Hospital",new Trust("Trust2"));
            UserGroup group = new UserGroup("Admin A","admin","123",hospital,true,"adminA@nhs.com");
            hospital.addGroup(group);
            Equipment equipment = new Equipment("Equipment3","Some description",hospital,"Neonatal","A");
            service.save(equipment);
        };
    }

    @Bean
    //DO NOT DELETE THIS
    //Used for returning buffered image to the frontend
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}
