package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.login.models.Hospital;
import team7.demo.login.services.HospitalService;
import team7.demo.login.services.TrustService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:3000/loginFail","http://localhost:3000/editUserGroup","http://localhost:3000/hospitalCreation"})
@RestController
@RequestMapping("/hospitals")
public class HospitalController {
    private final HospitalService service;
    private final TrustService trustService;

    @Autowired
    public HospitalController(HospitalService service,TrustService trustService){
        this.service = service;
        this.trustService = trustService;
    }

    @GetMapping("/all")
    public List<Hospital> getAll(){
        return service.getAll();
    }

    @GetMapping("/all/trustID={trustID}")
    public List<Hospital> getAllByTrust(@PathVariable long trustID){
        return service.getAllByTrust(trustID);
    }

    @PostMapping("/register/trustID={trustID} name={name}")
    public boolean register(@PathVariable long trustID,@PathVariable String name){
        if(checkStringIsInvalid(name)){
            return false;
        }
        service.save(new Hospital(name,trustService.findByID(trustID)));
        return true;
    }

    private boolean checkStringIsInvalid(String str){
        if (str == null||str.isEmpty()){
            return true;
        }
        return false;
    }

}
