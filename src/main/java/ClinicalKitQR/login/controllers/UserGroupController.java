package ClinicalKitQR.login.controllers;

import ClinicalKitQR.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.HospitalService;
import ClinicalKitQR.login.services.UserGroupService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
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
        String specialty = "";
        if (!group.getSpecialty().isEmpty()){
            specialty = " - " + group.getSpecialty();
        }
        if (group.getHospitalId().getHospitalName().equals("Trust Admin")){
            return "Trust Admin"+specialty;
        }else{
            if (group.getIsAdmin()){
                return "Hospital Admin"+specialty;
            }else {
                return "Normal User"+specialty;
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
    public String register(@RequestParam("hospitalId") long hospitalID,@RequestParam("name")  String name,
                            @RequestParam("username") String username,@RequestParam("password") String password,
                            @RequestParam("isAdmin") boolean isAdmin,@RequestParam("email") String email,
                            @RequestParam("specialty") String specialty){
    //request body is the data part of a request
        Hospital hospital = hospitalService.findByID(hospitalID);
        if (checkStringIsInvalid(name)||checkStringIsInvalid(username)||checkStringIsInvalid(password)
                ||hospital==null){
            return "Error: Invalid entries of required columns";
        }
        if (service.findByPK(hospitalID,username)!=null){
        //All usernames are unique
            return "Error: This username has already been occupied";
        }
        UserGroup group;
        if(specialty.isEmpty()){
            specialty = null;
            //set specialty to null if it is empty
        }
        group = new UserGroup(name,username,password,hospital,isAdmin,email,specialty);
        //assume that specialty passed is inside the database

        try {
            service.save(group);
            return "";
        }catch (Exception e){
            return "Error: "+e.getMessage();
        }

    }

    @PostMapping("/get")
    public UserGroup getById(@RequestParam("hospitalId")long hospitalId,@RequestParam("username") String username){
        return service.findByPK(hospitalId,username);
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
            groupls.add(group.getHospitalId().getTrust().getTrustName());
            result.add(groupls);
        }
        return result;
    }

    @PostMapping("/addTrust")
    public String addTrust(@RequestParam("trustName")String trustName,@RequestParam("username")String username,
                            @RequestParam("password")String password,@RequestParam("name")String name,
                            @RequestParam("email")String email,@RequestParam("specialty")String specialty){
        try{
            Trust newTrust = new Trust(trustName);
            Hospital newHospital = new Hospital("Trust Admin",newTrust);

            if (email.length()==0){
                email=null;
            }
            if(specialty.length() == 0){
                specialty = null;
            }
            UserGroup group = new UserGroup(name,username,password,newHospital,true,email,specialty);
            newHospital.addGroup(group);
            newTrust.addHospital(newHospital);

            service.save(group);
            return "";
        }catch (Exception e){
            return "Error: "+e.getMessage();
        }
    }

    @PostMapping("/update")
    public String update(@RequestParam("hospitalId")long hospitalId,@RequestParam("username")String username,
                          @RequestParam("name")String name,@RequestParam("password")String password,
                          @RequestParam("email")String email,@RequestParam("specialty")String specialty){
        UserGroup group = service.findByPK(hospitalId,username);
        String updateName = name;
        String updatePassword = password;
        String updateEmail = email;
        String updateSpecialty = specialty;
        if (group == null){
            return "Error: Your login details are invalid";
        }
        if (name.isEmpty()){
            updateName = group.getName();
        }
        if (password.isEmpty()){
            updatePassword = group.getPassword();
        }
        try{
            service.update(hospitalId,username,updateName,updatePassword,updateEmail,updateSpecialty);
            return "";
        }catch (Exception e){
            return "Error: "+e.getMessage();
        }
    }

    private boolean checkStringIsInvalid(String str){
        if (str == null||str.isEmpty()){
            return true;
        }
        return false;
    }
}
