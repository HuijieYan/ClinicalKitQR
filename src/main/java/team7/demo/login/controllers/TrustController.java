package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.Trust;
import team7.demo.login.services.TrustService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/"})
@RestController
@RequestMapping("/trusts")
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

    @PostMapping("/register/name={name}")
    public boolean register(@PathVariable String name){
        if(checkStringIsInvalid(name)){
            return false;
        }
        service.save(new Trust(name));
        return true;
    }

    private boolean checkStringIsInvalid(String str){
        if (str == null||str.isEmpty()){
            return true;
        }
        return false;
    }

}
