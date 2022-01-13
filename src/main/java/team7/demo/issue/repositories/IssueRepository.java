package team7.demo.issue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team7.demo.issue.models.Issue;

import javax.transaction.Transactional;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue,Long> {
    @Query("select i from Issue i where i.userGroupName.hospitalId = ?1")
    public List<Issue> getAllByHospitalId(long id);

    @Query("select i from Issue i where i.equipmentId.hospitalId.trust.trustId= ?1")
    public List<Issue> getAllByTrustId(long id);

    @Transactional
    @Modifying
    @Query("delete from Issue i where i.issueId = ?1")
    public void deleteByIssueId(long id);

    @Transactional
    @Modifying
    @Query("update Issue i set i.solved=?2 where i.issueId=?1")
    void updateSolved(long id,boolean solved);
}
