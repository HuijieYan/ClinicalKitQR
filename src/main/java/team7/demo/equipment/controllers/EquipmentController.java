package team7.demo.equipment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;

import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    private final EquipmentService service;

    @Autowired
    public EquipmentController(EquipmentService service){
        this.service = service;
    }

    @GetMapping(value = "/id={id}" ,produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> getEquipment(@PathVariable long id){
        try {
            return ResponseEntity.ok(service.generateQRCodeImage(id));
        }catch (Exception e){
            return null;
        }
    }
}
