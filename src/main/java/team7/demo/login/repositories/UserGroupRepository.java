package team7.demo.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team7.demo.login.models.UserGroup;
import team7.demo.login.models.UserGroupPrimaryKey;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, String> {
//this is used to access, manipulate database
    @Query("select u from UserGroup u where u.hospitalId.hospitalId = ?1 and u.username = ?2")
    UserGroup findByHospitalIdAndUsername(long id,String username);

    @Transactional //if update failed, nothing is going to change
    @Modifying
    @Query("update UserGroup u set u.name = ?4, u.password=?3 where u.username = ?2 and u.hospitalId.hospitalId = ?1")
    int updateUserGroup(long id,String username,String password,String name);

}
