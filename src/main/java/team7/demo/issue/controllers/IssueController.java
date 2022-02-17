package team7.demo.issue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;
import team7.demo.issue.models.Issue;
import team7.demo.issue.services.IssueService;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.UserGroupService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:3000/loginFail","http://localhost:3000/editUserGroup","http://localhost:3000/hospitalCreation"})
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
    public boolean updateSolved(@RequestParam("hospitalId") long hospitalId,@RequestParam("username") String username,
                             @RequestParam("equipmentId") long equipmentId,@RequestParam("description") String description){
        UserGroup group = userGroupService.findByPK(hospitalId,username);
        Equipment equipment = equipmentService.get(equipmentId);
        if (group==null||equipment==null){
            return false;
        }
        Issue issue = new Issue(LocalDate.now(),group,equipment,description);
        service.save(issue);
        return true;
    }

    @DeleteMapping("/delete/issueId={issueId}")
    public void deleteById(@PathVariable long issueId){
        service.delete(issueId);
    }
}
