package team7.demo.equipment.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;

import java.awt.image.BufferedImage;

@Configuration
public class EquipmentConfig {
    @Bean
    CommandLineRunner equipmentRunner(EquipmentService service){
        return args -> {
            Equipment equipment = new Equipment("Equipment1","Some description");
            service.save(equipment);
            equipment = new Equipment("Equipment2","Some description");
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
