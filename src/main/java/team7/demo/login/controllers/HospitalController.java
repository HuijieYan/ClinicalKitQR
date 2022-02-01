package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.login.models.Hospital;
import team7.demo.login.services.HospitalService;
import team7.demo.login.services.TrustService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/"})
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

    @GetMapping("/hospitalId={hospitalId}")
    public Hospital getHospital(@PathVariable long hospitalId){
        return service.findByID(hospitalId);
    }

    @GetMapping("/all/trustId={trustId}")
    public List<Hospital> getAllByTrust(@PathVariable long trustId){
        return service.getAllByTrust(trustId);
    }

    @PostMapping("/register/trustId={trustId} name={name}")
    public boolean register(@PathVariable long trustId,@PathVariable String name){
        if(checkStringIsInvalid(name)){
            return false;
        }
        service.save(new Hospital(name,trustService.findByID(trustId)));
        return true;
    }

    private boolean checkStringIsInvalid(String str){
        if (str == null||str.isEmpty()){
            return true;
        }
        return false;
    }

}
