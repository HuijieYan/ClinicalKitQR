package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.demo.login.models.Trust;
import team7.demo.login.services.TrustService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:3000/loginFail","http://localhost:3000/editUserGroup"})
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

}
