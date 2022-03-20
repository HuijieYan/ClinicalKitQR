package ClinicalKitQR.equipment.controllers;

import ClinicalKitQR.Constant;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.models.Manufacturer;
import ClinicalKitQR.equipment.services.EquipmentModelService;
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
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
@RestController
@RequestMapping(Constant.API_PREFIX+"/equipment")
public class EquipmentController {
    private final EquipmentService service;
    private final UserGroupService userGroupService;
    private final ViewingService viewingService;
    private final ManufacturerService manufacturerService;
    private final EquipmentModelService modelService;

    @Autowired
    public EquipmentController(EquipmentService service,UserGroupService userGroupService, ViewingService viewingService,
                               ManufacturerService manufacturerService,EquipmentModelService modelService){
        this.service = service;
        this.manufacturerService = manufacturerService;
        this.userGroupService = userGroupService;
        this.viewingService = viewingService;
        this.modelService = modelService;
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
        return service.getAllByTrust(id);
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
                       @RequestParam("model")String modelName,@RequestParam("manufacturer")String manufacturerName){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return "Failed to save the equipment, error: User does not exist or login session expired";
        }
        try{
            Hospital hospital = group.getHospitalId();
            Manufacturer manufacturer = manufacturerService.getByName(manufacturerName);
            if(manufacturer==null){
                manufacturer = new Manufacturer(manufacturerName);
                manufacturerService.save(manufacturer);
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
                         @RequestParam("equipmentId")long equipmentId,@RequestParam("model")String modelName,
                         @RequestParam("manufacturer")String manufacturerName){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return "Failed to update the equipment, error: User does not exist or login session expired";
        }
        try{
            Equipment equipment = service.get(equipmentId);
            if (equipment.getHospitalId().getHospitalId()==hospitalId||(group.getHospitalId().getHospitalName().equals("Trust Admin")&&group.getHospitalId().getTrust().getTrustId()==equipment.getHospitalId().getTrust().getTrustId())){
                EquipmentModel model = equipment.getModel();
                if(!modelName.equals(model.getModelName())){
                    model.setModelName(modelName);
                }
                //if model has changed, update the model

                Manufacturer manufacturer = model.getManufacturer();
                if(!manufacturerName.equals(manufacturer.getManufacturerName())){
                    manufacturer.removeModel(model);

                    Manufacturer newManufacturer = manufacturerService.getByName(manufacturerName);
                    if(newManufacturer==null){
                        newManufacturer = new Manufacturer(manufacturerName);
                    }
                    model.setManufacturer(newManufacturer);
                }
                //if the manufacturer has changed, see if the new manufacturer already exists in the database or not

                service.updateModel(equipmentId,model);

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
                                  @RequestParam("manufacturer") String manufacturerName,@RequestParam("model")String modelName,
                                  @RequestParam("name") String name){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return null;
        }
        return service.search(group,type,category,name,manufacturerName,modelName);
    }

    @PostMapping("/manufacturers/all")
    public String[] getAllManufacturers(@RequestParam("hospitalId") long hospitalId, @RequestParam("username")String username){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return null;
        }
        List<Manufacturer> manufacturers = manufacturerService.getAll();
        String[] data = new String[manufacturers.size()];
        int index = 0;
        for(Manufacturer manufacturer:manufacturers){
            data[index] = manufacturer.getManufacturerName();
            index++;
        }
        return data;
    }

    @PostMapping("/models/getByUser")
    public String[] getModelsByUser(@RequestParam("hospitalId") long hospitalId, @RequestParam("username")String username){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return null;
        }
        List<EquipmentModel> models = new ArrayList<>();
        if(group.getHospitalId().getHospitalName().equals("Trust Admin")){
            models =  modelService.getModelsByTrust(group.getHospitalId().getTrust().getTrustId());
        }else{
            models =  modelService.getModelsByHospital(group.getHospitalId().getHospitalId());
        }

        String[] data = new String[models.size()];
        int index = 0;
        for(EquipmentModel model:models){
            data[index] = model.getModelName();
            index++;
        }
        return data;
    }

    @PostMapping("/models/getByManufacturer")
    public String[] getModelsByManufacturer(@RequestParam("hospitalId") long hospitalId, @RequestParam("username")String username,
                                                @RequestParam("manufacturer") String manufactureName){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        if (group == null){
            return null;
        }
        List<EquipmentModel> models = new ArrayList<>();
        if(group.getHospitalId().getHospitalName().equals("Trust Admin")){
            models = modelService.getModelsByTrustAndManufacture(group.getHospitalId().getTrust().getTrustId(),manufactureName);
        }else{
            models = modelService.getModelsByHospitalAndManufacture(group.getHospitalId().getHospitalId(),manufactureName);
        }

        String[] data = new String[models.size()];
        int index = 0;
        for(EquipmentModel model:models){
            data[index] = model.getModelName();
            index++;
        }
        return data;
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
