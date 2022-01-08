package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.HospitalService;
import team7.demo.login.services.TrustService;
import team7.demo.login.services.UserGroupService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:3000/loginFail","http://localhost:3000/editUserGroup"})
@RestController
@RequestMapping("/usergroup")
public class UserGroupController {
    private final UserGroupService service;
    private final HospitalService hospitalService;

    @Autowired
    public UserGroupController(UserGroupService service,HospitalService hospitalService){
        this.service = service;
        this.hospitalService = hospitalService;
    }

    @GetMapping("/login/hospitalID={hospitalID} username={username} password={password}")
    public List<Integer> login(@PathVariable long hospitalID, @PathVariable String username, @PathVariable String password){
        List<Integer> result = new ArrayList<>();
        UserGroup group = service.findByPK(hospitalID,username);
        if(group!=null&&group.getPassword().equals(password)){
            result.add(1);
            if (group.getHospital().getHospitalName().equals("Trust Admin")){
                result.add(3);
            }else {
                if (group.getIsAdmin()){
                    result.add(2);
                }else{
                    result.add(1);
                }
            }
            return result;
        }
        result.add(0);
        result.add(-1);
        return result;
    }

    @PostMapping("/register/trustID={trustID} hospitalID={hospitalID} name={name} username={username} password={password} isAdmin={isAdmin}")
    public boolean register(@PathVariable long trustID,@PathVariable long hospitalID,@PathVariable String name,@PathVariable String username,@PathVariable String password,@PathVariable boolean isAdmin){
    //request body is the data part of a request
        Hospital hospital = hospitalService.findByID(hospitalID);
        if (checkStringIsInvalid(name)||checkStringIsInvalid(username)||checkStringIsInvalid(password)
                ||hospital==null){
            return false;
        }
        if (service.findByPK(hospitalID,username)!=null){
        //All usernames are unique
            return false;
        }
        UserGroup group = new UserGroup(name,username,password,hospital,isAdmin);
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
