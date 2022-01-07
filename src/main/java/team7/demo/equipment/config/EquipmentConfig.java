package team7.demo.equipment.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.Trust;

import java.awt.image.BufferedImage;

@Configuration
public class EquipmentConfig {
    @Bean
    CommandLineRunner equipmentRunner(EquipmentService service){
        return args -> {
            Hospital hospital = new Hospital("New Hospital",new Trust("Trust2"));
            Equipment equipment = new Equipment("Equipment3","Some description",hospital);
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
