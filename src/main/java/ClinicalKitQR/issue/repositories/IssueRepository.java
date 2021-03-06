package ClinicalKitQR.issue.repositories;

import ClinicalKitQR.issue.models.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue,Long> {
    @Query("select i from Issue i where i.userGroupName.hospitalId = ?1 order by i.issueId DESC")
    List<Issue> getAllByHospitalId(long id);

    @Query("select i from Issue i where i.equipmentId.hospitalId.trust.trustId= ?1 order by i.issueId DESC")
    List<Issue> getAllByTrustId(long id);

    //ordering by issue ID so the latest will always come first

    @Transactional
    @Modifying
    @Query("delete from Issue i where i.issueId = ?1")
    void deleteByIssueId(long id);

    @Transactional
    @Modifying
    @Query("update Issue i set i.solved=?2 where i.issueId=?1")
    void updateSolved(long id,boolean solved);
}
