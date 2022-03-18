package ClinicalKitQR.issue.controllers;

import ClinicalKitQR.Constant;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.issue.models.Issue;
import ClinicalKitQR.issue.services.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.UserGroupService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
@RestController
@RequestMapping("/issues")
public class IssueController {
    private final IssueService service;
    private final UserGroupService userGroupService;
    private final EquipmentService equipmentService;

    @Autowired
    public IssueController(IssueService service,UserGroupService userGroupService,EquipmentService equipmentService){
        this.service = service;
        this.userGroupService = userGroupService;
        this.equipmentService = equipmentService;
    }

    @GetMapping("/hospitalId={hospitalId}")
    public List<Issue> getByHospital(@PathVariable long hospitalId){
        return service.getAllByHospital(hospitalId);
    }

    @GetMapping("/trustId={trustId}")
    public List<Issue> getByTrust(@PathVariable long trustId){
        return service.getAllByTrust(trustId);
    }

    @PostMapping("/issueId={issueId}")
    public void updateSolved(@PathVariable long issueId,@RequestParam("solved") boolean solved){
        service.updateSolved(issueId, solved);
    }

    @PostMapping("/new")
    public String addNewIssue(@RequestParam("hospitalId") long hospitalId,@RequestParam("username") String username,
                             @RequestParam("equipmentId") long equipmentId,@RequestParam("description") String description){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        Equipment equipment = equipmentService.get(equipmentId);
        if (group==null){
            return "Error: The user group is not found";
        }
        if(equipment==null){
            return "Error: The equipment is not found";
        }
        if(checkStringIsInvalid(description)){
            return "Error: The description entered is empty";
        }

        Issue issue = new Issue(LocalDate.now(),group,equipment,description);
        service.save(issue);
        return "";
    }

    @DeleteMapping("/delete/issueId={issueId}")
    public void deleteById(@PathVariable long issueId){
        service.delete(issueId);
    }

    private boolean checkStringIsInvalid(String str){
        if (str == null||str.isEmpty()){
            return true;
        }
        return false;
    }
}
