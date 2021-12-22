package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.HospitalService;
import team7.demo.login.services.TrustService;
import team7.demo.login.services.UserGroupService;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:3000/loginFail","http://localhost:3000/editUserGroup"})
@RestController
@RequestMapping
public class UserGroupController {
    private final UserGroupService service;
    private final HospitalService hospitalService;
    private final TrustService trustService;

    @Autowired
    public UserGroupController(UserGroupService service,HospitalService hospitalService,TrustService trustService){
        this.service = service;
        this.hospitalService = hospitalService;
        this.trustService = trustService;
    }

    @GetMapping("/login/hospitalID={hospitalID} username={username} password={password}")
    public boolean login(@PathVariable long hospitalID,@PathVariable String username,@PathVariable String password){
        return service.login(hospitalID,username,password);
    }

    @PostMapping("/addUsergroup/trustID={trustID} hospitalID={hospitalID} name={name} username={username} password={password}")
    public boolean registerNewUserGroup(@PathVariable long trustID,@PathVariable long hospitalID,@PathVariable String name,@PathVariable String username,@PathVariable String password){
    //request body is the data part of a request
        Hospital hospital = hospitalService.findByID(hospitalID);
        if (checkStringIsInvalid(name)||checkStringIsInvalid(username)||checkStringIsInvalid(password)
                ||hospital==null||trustService.findByID(trustID)==null){
            return false;
        }
        if (service.findByPK(hospitalID,username)!=null){
        //All usernames are unique
            return false;
        }
        UserGroup group = new UserGroup(name,username,password,hospital);
        //hospital.addGroup(group);
        service.save(group);
        return true;
    }

    private boolean checkStringIsInvalid(String str){
        if (str == null||str.isEmpty()){
            return true;
        }
        return false;
    }
}
