package team7.demo.equipment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;
import team7.demo.equipment.services.SentEquipmentService;
import team7.demo.login.models.Hospital;
import team7.demo.login.services.HospitalService;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/sentEquipment")
public class SentEquipmentController {
    private final SentEquipmentService service;
    private final EquipmentService equipmentService;
    private final HospitalService hospitalService;

    @Autowired
    public SentEquipmentController(SentEquipmentService service,EquipmentService equipmentService,HospitalService hospitalService){
        this.service = service;
        this.equipmentService = equipmentService;
        this.hospitalService = hospitalService;
    }

    @PostMapping("/saving")
    public void saving(@RequestParam("hospitalId") long hospitalId,@RequestParam("ids")String[] ids){
        Hospital hospital = hospitalService.findByID(hospitalId);
        if (hospital==null){
            return;
        }
        for (String id:ids){
            service.updateSaved(id);
            Equipment equipment = new Equipment(service.getById(id),hospital);
            equipmentService.save(equipment);
        }
    }
}
