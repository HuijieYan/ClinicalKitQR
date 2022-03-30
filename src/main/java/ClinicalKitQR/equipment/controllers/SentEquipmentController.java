package ClinicalKitQR.equipment.controllers;

import ClinicalKitQR.Constant;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.Manufacturer;
import ClinicalKitQR.equipment.models.SentEquipment;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.equipment.services.ManufacturerService;
import ClinicalKitQR.equipment.services.SentEquipmentService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
@RestController
@RequestMapping(Constant.API_PREFIX+"/sentEquipment")
public class SentEquipmentController {
    private final SentEquipmentService service;
    private final EquipmentService equipmentService;
    private final HospitalService hospitalService;
    private final ManufacturerService manufacturerService;

    @Autowired
    public SentEquipmentController(SentEquipmentService service,EquipmentService equipmentService,
                                   HospitalService hospitalService,ManufacturerService manufacturerService){
        this.service = service;
        this.equipmentService = equipmentService;
        this.hospitalService = hospitalService;
        this.manufacturerService = manufacturerService;
    }

    @PostMapping("/saving")
    public void saving(@RequestParam("hospitalId") long hospitalId,@RequestParam("ids")String[] ids){
        Hospital hospital = hospitalService.findByID(hospitalId);
        if (hospital==null){
            return;
        }
        for (String id:ids){
            SentEquipment sentEquipment = service.getById(id);
            if(sentEquipment!=null){
                Manufacturer manufacturer = manufacturerService.getByName(sentEquipment.getManufacturer());
                if(manufacturer==null){
                    manufacturer = new Manufacturer(sentEquipment.getManufacturer());
                }
                if(!sentEquipment.getSaved()){
                    Equipment equipment = new Equipment(sentEquipment,hospital,manufacturer);
                    equipmentService.save(equipment);
                    service.updateSaved(id);
                }
            }

        }
    }

    @PostMapping("/get")
    public SentEquipment get(@RequestParam("id") String id){
        return service.getById(id);
    }
}
