package team7.demo.issue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team7.demo.issue.models.Issue;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue,Long> {
    @Query("select i from Issue i where i.userGroupName.hospitalId = ?1")
    public List<Issue> getAllByHospitalId(long id);
}
