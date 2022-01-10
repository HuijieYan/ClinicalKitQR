package team7.demo.equipment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;

import java.awt.image.BufferedImage;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    private final EquipmentService service;

    @Autowired
    public EquipmentController(EquipmentService service){
        this.service = service;
    }

    @GetMapping(value = "/qrcode/id={id}" ,produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> getEquipment(@PathVariable long id){
        try {
            return ResponseEntity.ok(service.generateQRCodeImage(id));
        }catch (Exception e){
            return null;
        }
    }

    @GetMapping(value = "/trustId={id}")
    public List<Equipment> getEquipmentsInTrust(@PathVariable long id){
        List<Equipment> ls = service.getAllByTrust(id);
        return ls;
    }

    @GetMapping(value = "/hospitalId={id}")
    public List<Equipment> getEquipmentsInHospital(@PathVariable long id){
        return service.getAllByHospital(id);
    }

    @GetMapping("/all")
    public List<Equipment> getAll(){
        return service.getAll();
    }

    @DeleteMapping("/delete/id={id}")
    public void deleteById(@PathVariable long id){
        service.delete(id);
    }
}
