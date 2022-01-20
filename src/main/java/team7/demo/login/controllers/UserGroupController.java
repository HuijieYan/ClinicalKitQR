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

    @GetMapping("/hospitalId={hospitalId}")
    public List<List<String>> getAllByHospital(@PathVariable long hospitalId){
        List<List<String>> result = new ArrayList<>();
        List<UserGroup> groups = service.getAllByHospital(hospitalId);
        for (UserGroup group:groups){
            List<String> groupls = new ArrayList<>();
            groupls.add(group.getName());
            groupls.add(group.getUsername());
            groupls.add(getRole(group));
            groupls.add(group.getHospitalId().getHospitalName());
            groupls.add(Long.toString(group.getHospitalId().getHospitalId()));
            result.add(groupls);
        }
        return result;
    }

    private String getRole(UserGroup group){
        if (group.getHospitalId().getHospitalName().equals("Trust Admin")){
            return "Trust Admin";
        }else{
            if (group.getIsAdmin()){
                return "Hospital Admin";
            }else {
                return "Normal User";
            }
        }
    }

    @GetMapping("/trustId={trustId}")
    public List<List<String>> getAllByTrust(@PathVariable long trustId){
        List<List<String>> result = new ArrayList<>();
        List<UserGroup> groups = service.getAllByTrust(trustId);
        for (UserGroup group:groups){
            List<String> groupls = new ArrayList<>();
            groupls.add(group.getName());
            groupls.add(group.getUsername());
            groupls.add(getRole(group));
            groupls.add(group.getHospitalId().getHospitalName());
            groupls.add(Long.toString(group.getHospitalId().getHospitalId()));
            result.add(groupls);
        }
        return result;
    }

    @DeleteMapping("/delete/hospitalId={hospitalId} username={username}")
    public void delete(@PathVariable long hospitalId,@PathVariable String username){
        service.delete(hospitalId,username);
    }

    @GetMapping("/login/hospitalID={hospitalID} username={username} password={password}")
    public List<String> login(@PathVariable long hospitalID, @PathVariable String username, @PathVariable String password){
        List<String> result = new ArrayList<>();
        UserGroup group = service.findByPK(hospitalID,username);
        if(group!=null&&group.getPassword().equals(password)){
            if (group.getHospitalId().getHospitalName().equals("Trust Admin")){
                result.add(Integer.toString(3));
            }else {
                if (group.getIsAdmin()){
                    result.add(Integer.toString(2));
                }else{
                    result.add(Integer.toString(1));
                }
            }
            result.add(Long.toString(group.getHospitalId().getHospitalId()));
            result.add(Long.toString(group.getHospitalId().getTrust().getTrustId()));
            result.add(group.getName());
            return result;
        }
        return result;
    }

    @PostMapping("/register/trustID={trustID} hospitalID={hospitalID} name={name} username={username} password={password} isAdmin={isAdmin} email={email} speciality={speciality}")
    public boolean register(@PathVariable long trustID,@PathVariable long hospitalID,@PathVariable String name,
                            @PathVariable String username,@PathVariable String password,
                            @PathVariable boolean isAdmin,@PathVariable String email,
                            @PathVariable String speciality){
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
        UserGroup group;
        group = new UserGroup(name,username,password,hospital,isAdmin,email,speciality);

        service.save(group);
        return true;
    }

    @GetMapping("/all/admins")
    public List<List<String>> getAllAdmin(){
        List<List<String>> result = new ArrayList<>();
        List<UserGroup> groups = service.getAllAdmins();
        for (UserGroup group:groups){
            List<String> groupls = new ArrayList<>();
            groupls.add(group.getName());
            groupls.add(group.getHospitalId().getHospitalName());
            groupls.add(getRole(group));
            groupls.add(Long.toString(group.getHospitalId().getHospitalId()));
            groupls.add(group.getEmail());
            result.add(groupls);
        }
        return result;
    }

    private boolean checkStringIsInvalid(String str){
        if (str == null||str.isEmpty()){
            return true;
        }
        return false;
    }
}
