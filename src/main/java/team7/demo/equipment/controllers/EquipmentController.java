package team7.demo.equipment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.HospitalService;
import team7.demo.login.services.UserGroupService;

import java.awt.image.BufferedImage;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    private final EquipmentService service;
    private final HospitalService hospitalService;
    private final UserGroupService userGroupService;

    @Autowired
    public EquipmentController(EquipmentService service,HospitalService hospitalService,UserGroupService userGroupService){
        this.service = service;
        this.hospitalService = hospitalService;
        this.userGroupService = userGroupService;
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

    @PostMapping("/save/name={name} content={content} hospitalId={hospitalId}")
    public void save(@PathVariable String name,@PathVariable String content,@PathVariable long hospitalId){
        Hospital hospital = hospitalService.findByID(hospitalId);
        Equipment equipment = new Equipment(name,content,hospital,"Neonatal","A");
        service.save(equipment);
    }

    @PostMapping("/search")
    public List<Equipment> search(@RequestParam("hospitalId") long hospitalId,@RequestParam("username") String username,
                                  @RequestParam("type") String type,@RequestParam("category")String category,
                                  @RequestParam("name") String name){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
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
