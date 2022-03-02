package ClinicalKitQR.equipment.controllers;

import ClinicalKitQR.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.SentEquipment;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.equipment.services.SentEquipmentService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.services.HospitalService;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
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

    @PostMapping("/get")
    public SentEquipment get(@RequestParam("id") String id){
        return service.getById(id);
    }
}
