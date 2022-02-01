package team7.demo.issue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.issue.models.Issue;
import team7.demo.issue.services.IssueService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:3000/loginFail","http://localhost:3000/editUserGroup","http://localhost:3000/hospitalCreation"})
@RestController
@RequestMapping("/issues")
public class IssueController {
    private final IssueService service;

    @Autowired
    public IssueController(IssueService service){
        this.service = service;
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

    @DeleteMapping("/delete/issueId={issueId}")
    public void deleteById(@PathVariable long issueId){
        service.delete(issueId);
    }
}
