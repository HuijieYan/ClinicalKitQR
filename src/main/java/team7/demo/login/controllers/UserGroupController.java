package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.UserGroupService;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:3000/loginFail"})
@RestController
@RequestMapping("/login")
public class UserGroupController {
    private final UserGroupService service;

    @Autowired
    public UserGroupController(UserGroupService service){
        this.service = service;
    }

    @GetMapping("/hospitalID={hospitalID} username={username} password={password}")
    public boolean login(@PathVariable long hospitalID,@PathVariable String username,@PathVariable String password){
        return service.login(hospitalID,username,password);
    }

    @PostMapping
    public void registerNewUserGroup(@RequestBody UserGroup group){
    //request body is the data part of a request
        if (checkStringIsInvalid(group.getUsername())||checkStringIsInvalid(group.getName())||checkStringIsInvalid(group.getPassword())){
            return;
        }
        if (service.findByPK(group.getHospital().getId(),group.getUsername())!=null){
        //All usernames are unique
            return;
        }
        service.save(group);
    }

    private boolean checkStringIsInvalid(String str){
        if (str == null||str.length()>0){
            return true;
        }
        return false;
    }
}
