package ClinicalKitQR.equipment.controllers;

import ClinicalKitQR.Constant;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.models.Manufacturer;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.equipment.services.ManufacturerService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.viewing.services.ViewingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ClinicalKitQR.login.services.UserGroupService;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    private final EquipmentService service;
    private final UserGroupService userGroupService;
    private final ViewingService viewingService;
    private final ManufacturerService manufacturerService;

    @Autowired
    public EquipmentController(EquipmentService service,UserGroupService userGroupService, ViewingService viewingService,
                               ManufacturerService manufacturerService){
        this.service = service;
        this.manufacturerService = manufacturerService;
        this.userGroupService = userGroupService;
        this.viewingService = viewingService;
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

    @PostMapping("/save")
    public String save(@RequestParam("name") String name,@RequestParam("content") String content,@RequestParam("hospitalId") long hospitalId,
                       @RequestParam("type")String type,@RequestParam("category")String category,@RequestParam("username")String username,
                       @RequestParam("Model")String modelName,@RequestParam("manufacturer")String manufacturerName){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return "Failed to save the equipment, error: User does not exist or login session expired";
        }
        try{
            Hospital hospital = group.getHospitalId();
            Manufacturer manufacturer = manufacturerService.getByName(manufacturerName);
            if(manufacturer==null){
                manufacturer = new Manufacturer(manufacturerName);
            }
            Equipment equipment = new Equipment(name,content,hospital,type,category,new EquipmentModel(modelName,manufacturer));
            service.save(equipment);
            return "Equipment Saved Successfully";
        }catch(Exception e){
            return "Failed to save the equipment, error: "+e.getMessage();
        }
    }

    @PostMapping("/copy")
    public String copy(@RequestParam("equipmentId") long id,@RequestParam("hospitalId") long hospitalId,
                       @RequestParam("username")String username){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return "Failed to save the equipment, error: User does not exist or login session expired";
        }
        try{
            Equipment equipment = service.get(id);
            Equipment copy = new Equipment(equipment);
            service.save(copy);
            return "Equipment Saved Successfully";
        }catch(Exception e){
            return "Failed to save the equipment, error: "+e.getMessage();
        }
    }

    @PostMapping("/update")
    public String update(@RequestParam("name") String name,@RequestParam("content") String content,@RequestParam("hospitalId") long hospitalId,
                       @RequestParam("type")String type,@RequestParam("category")String category,@RequestParam("username")String username,
                         @RequestParam("equipmentId")long equipmentId){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return "Failed to update the equipment, error: User does not exist or login session expired";
        }
        try{
            Equipment equipment = service.get(equipmentId);
            if (equipment.getHospitalId().getHospitalId()==hospitalId||(group.getHospitalId().getHospitalName().equals("Trust Admin")&&group.getHospitalId().getTrust().getTrustId()==equipment.getHospitalId().getTrust().getTrustId())){
                service.update(equipmentId,name,content,type,category);
                return "Equipment Updated Successfully";
            }else {
                //if the user is not the admin of this equipment's trust
                return "Failed to update the equipment, error: Your user group has no right to edit this equipment";
            }
        }catch(Exception e){
            return "Failed to update the equipment, error: "+e.getMessage();
        }
    }

    @PostMapping("/get")
    public Equipment getById(@RequestParam("id") long id,@RequestParam("hospitalId") long hospitalId, @RequestParam("username")String username){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return null;
        }
        Equipment equipment = service.get(id);
        if (equipment.getHospitalId().getHospitalId()==hospitalId){
            if (!group.getIsAdmin()) {
                // creates new viewing if the user is not an admin (therefore does not increment on editing equipment)
                viewingService.addNewView(equipment, LocalDate.now(),group);
            }
            return equipment;
        }else if(group.getHospitalId().getHospitalName().equals("Trust Admin")&&group.getHospitalId().getTrust().getTrustId()==equipment.getHospitalId().getTrust().getTrustId()){
            //if the user is the admin of this equipment's trust
            return equipment;
        }else{
            return null;
            //null represents the user group has no right to access this equipment or the equipment is not found
        }
    }

    @PostMapping("/search")
    public List<Equipment> search(@RequestParam("hospitalId") long hospitalId,@RequestParam("username") String username,
                                  @RequestParam("type") String type,@RequestParam("category")String category,
                                  @RequestParam("name") String name){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return null;
        }
        return service.search(group,type,category,name);
    }

    @GetMapping("/types")
    public String[] getTypes(){
        return service.getTypes();
    }
    @GetMapping("/categories")
    public String[] getCategories(){
        return service.getCategories();
    }
}
