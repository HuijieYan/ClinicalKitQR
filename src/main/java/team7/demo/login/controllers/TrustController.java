package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.Trust;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.HospitalService;
import team7.demo.login.services.TrustService;
import team7.demo.login.services.UserGroupService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/"})
@RestController
@RequestMapping("/trusts")
public class TrustController {
    private final TrustService service;
    private final EquipmentService equipmentService;
    private final UserGroupService userGroupService;

    @Autowired
    public TrustController(TrustService service,EquipmentService equipmentService,UserGroupService userGroupService){
        this.service = service;
        this.equipmentService = equipmentService;
        this.userGroupService = userGroupService;
    }

    @GetMapping("/all")
    public List<Trust> getAll(){
        List<Trust> trusts = service.getAll();
        return trusts;
    }

    @PostMapping("/register/name={name}")
    public boolean register(@PathVariable String name){
        if(checkStringIsInvalid(name)){
            return false;
        }
        service.save(new Trust(name));
        return true;
    }

    @PostMapping("/get")
    public Trust getById(@RequestParam("id")long id){
        return service.findByID(id);
    }

    @GetMapping("/all/admins")
    public List<UserGroup> getAllAdminsInOrder(){
        List<Trust> trusts = service.getAll();
        List<UserGroup> groups = new ArrayList<>();
        for(Trust trust:trusts){
            List<Hospital> hospitals = trust.getHospitals();
            for (Hospital hospital:hospitals){
               groups.addAll(hospital.getGroups());
            }
        }
        return groups;
    }

    @PostMapping("/delete")
    public boolean delete(@RequestParam("id") long id){
        try {
            Trust trust = service.findByID(id);
            if (trust == null){
                return false;
            }
            service.delete(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private boolean checkStringIsInvalid(String str){
        if (str == null||str.isEmpty()){
            return true;
        }
        return false;
    }

}
