package ClinicalKitQR.login.controllers;

import ClinicalKitQR.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.TrustService;
import ClinicalKitQR.login.services.UserGroupService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
@RestController
@RequestMapping(Constant.API_PREFIX+"/trusts")
public class TrustController {
    private final TrustService service;
    @Autowired
    public TrustController(TrustService service){
        this.service = service;
    }

    @GetMapping("/all")
    public List<Trust> getAll(){
        List<Trust> trusts = service.getAll();
        return trusts;
    }

    @PostMapping("/addTrust")
    public String addTrust(@RequestParam("trustName")String trustName,@RequestParam("username")String username,
                           @RequestParam("password")String password,@RequestParam("name")String name,
                           @RequestParam("email")String email,@RequestParam("specialty")String specialty){
        try{
            if(checkStringIsInvalid(trustName)){
                return "Error: New trust's name is invalid";
            }
            Trust newTrust = new Trust(trustName);
            Hospital newHospital = newTrust.getHospitals().get(0);

            if(checkStringIsInvalid(username)||checkStringIsInvalid(password)||checkStringIsInvalid(name)){
                return "Error: New trust's trust admin details are invalid";
            }
            if (email == null){
                email = "";
            }
            if(specialty == null){
                specialty = "";
            }
            UserGroup group = new UserGroup(name,username,password,newHospital,true,email,specialty);
            newHospital.addGroup(group);
            newTrust.addHospital(newHospital);

            service.save(newTrust);
            return "";
        }catch (Exception e){
            return "Error: "+e.getMessage();
        }
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
            if(service.findByID(id)==null){
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
