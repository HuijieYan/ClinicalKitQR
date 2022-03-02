package ClinicalKitQR.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ClinicalKitQR.login.models.UserGroup;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, String> {
//this is used to access, manipulate database
    @Query("select u from UserGroup u where u.hospitalId.hospitalId = ?1 and u.username = ?2")
    UserGroup findByHospitalIdAndUsername(long id,String username);

    @Transactional //if update failed, nothing is going to change
    @Modifying
    @Query("update UserGroup u set u.name = ?3, u.password=?4,u.email=?5,u.specialty=?6 where u.username = ?2 and u.hospitalId.hospitalId = ?1")
    int updateUserGroup(long id,String username,String name,String password,String email,String specialty);

    @Query("select u from UserGroup u where u.hospitalId.hospitalId = ?1")
    List<UserGroup> findAllByHospitalId(long id);

    @Query("select u from UserGroup u where u.hospitalId.trust.trustId = ?1")
    List<UserGroup> findAllByTrustId(long id);

    @Query("select u from UserGroup u where u.isAdmin = true")
    List<UserGroup> findALlAdmins();


    @Transactional
    @Modifying
    @Query("delete from UserGroup u where u.username=?2 and u.hospitalId.hospitalId=?1")
    void deleteByPK(long hospitalId,String username);
}
