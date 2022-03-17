package ClinicalKitQR.equipment.config;

import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.models.Manufacturer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
    //DO NOT DELETE THIS
    //Used for returning buffered image to the frontend
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}
