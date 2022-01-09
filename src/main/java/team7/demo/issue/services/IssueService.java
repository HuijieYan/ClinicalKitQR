package team7.demo.issue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.issue.models.Issue;
import team7.demo.issue.repositories.IssueRepository;

import java.util.List;

@Service
public class IssueService {
    private final IssueRepository repository;

    @Autowired
    public IssueService(IssueRepository repository){
        this.repository = repository;
    }

    public List<Issue> getAllByHospital(long id){
        return repository.getAllByHospitalId(id);
    }

    public void save(Issue issue){
        issue.getEquipmentId().addIssue(issue);
        issue.getUserGroupName().addIssue(issue);
        repository.save(issue);
    }

}
