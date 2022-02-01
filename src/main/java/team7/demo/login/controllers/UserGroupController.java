package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.HospitalService;
import team7.demo.login.services.SpecialtyService;
import team7.demo.login.services.TrustService;
import team7.demo.login.services.UserGroupService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/"})
@RestController
@RequestMapping("/usergroup")
public class UserGroupController {
    private final UserGroupService service;
    private final HospitalService hospitalService;
    private final SpecialtyService specialtyService;

    @Autowired
    public UserGroupController(UserGroupService service,HospitalService hospitalService,SpecialtyService specialtyService){
        this.service = service;
        this.hospitalService = hospitalService;
        this.specialtyService = specialtyService;
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

    @PostMapping("/login")
    public List<String> login(@RequestParam("hospitalId") String hospitalId, @RequestParam("username") String username, @RequestParam("password") String password){
        List<String> result = new ArrayList<>();
        long HospitalID = Long.parseLong(hospitalId);
        UserGroup group = service.findByPK(HospitalID,username);
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

    @PostMapping("/register")
    public boolean register(@RequestParam("trustId") long trustID,@RequestParam("hospitalID") long hospitalID,@RequestParam("name")  String name,
                            @RequestParam("username") String username,@RequestParam("password") String password,
                            @RequestParam("isAdmin") boolean isAdmin,@RequestParam("email") String email,
                            @RequestParam("speciality") String speciality){
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
        group = new UserGroup(name,username,password,hospital,isAdmin,email,specialtyService.findByName(speciality));
        //assume that specialty passed is inside the database

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
            groupls.add(group.getUsername());
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
