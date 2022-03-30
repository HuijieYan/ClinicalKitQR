package ClinicalKitQR.login.controllers;

import ClinicalKitQR.Constant;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.services.HospitalService;
import ClinicalKitQR.login.services.TrustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
@RestController
@RequestMapping(Constant.API_PREFIX+"/hospitals")
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

    @PostMapping("/new")
    public boolean register(@RequestParam("id") long trustId,@RequestParam("name") String name){
        if(checkStringIsInvalid(name)||name.equals("Trust Admin")){
            return false;
        }
        service.save(new Hospital(name,trustService.findByID(trustId)));
        return true;
    }

    @PostMapping("/delete")
    public boolean delete(@RequestParam("id")long id){
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

    @PostMapping("/update")
    public boolean update(@RequestParam("id")long id,@RequestParam("name")String newName){
        try {
            if(checkStringIsInvalid(newName)){
                return false;
            }
            service.update(id,newName);
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
