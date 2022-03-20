package ClinicalKitQR.viewing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.viewing.models.EquipmentViewing;
import ClinicalKitQR.viewing.models.Viewing;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface ViewingRepository extends JpaRepository<Viewing, Long> {

    // equipment only queries

    @Query("select new ClinicalKitQR.viewing.models.EquipmentViewing(v.userGroup, sum(v.viewCounter))" +
            " from Viewing v where v.equipmentId.equipmentId = ?1 group by v.userGroup")
    List<EquipmentViewing> getAllByEquipmentId(Long id);


    @Query("select new ClinicalKitQR.viewing.models.EquipmentViewing(v.userGroup, sum(v.viewCounter))" +
            " from Viewing v where v.equipmentId.equipmentId = ?1 and v.date between ?2 and ?3 group by v.userGroup")
    List<EquipmentViewing> getAllByEquipmentIdAndDateBetween(Long id, LocalDate startDate, LocalDate endDate);


    @Query("select new ClinicalKitQR.viewing.models.EquipmentViewing(v.userGroup, sum(v.viewCounter))" +
            " from Viewing v where v.equipmentId.equipmentId = ?1 and v.date <= ?2 group by v.userGroup")
    List<EquipmentViewing> getAllByEquipmentIdAndDateBefore(Long id, LocalDate endDate);

    @Query("select new ClinicalKitQR.viewing.models.EquipmentViewing(v.userGroup, sum(v.viewCounter))" +
            " from Viewing v where v.equipmentId.equipmentId = ?1 and v.date >= ?2 group by v.userGroup")
    List<EquipmentViewing> getAllByEquipmentIdAndDateAfter(Long equipmentId, LocalDate startDate);

    // user group only queries

    @Query("select v from Viewing v where v.userGroup.hospitalId = ?1 and v.userGroup.username = ?2")
    List<Viewing> getAllByUserGroup(Long hospitalId, String username);

    @Query("select v from Viewing v where v.userGroup.hospitalId = ?1 and v.userGroup.username = ?2 and v.date between ?3 and ?4")
    List<Viewing> findAllByUserGroupAndDateBetween(Long hospitalId, String username, LocalDate startDate, LocalDate endDate);

    @Query("select v from Viewing v where v.userGroup.hospitalId = ?1 and v.userGroup.username = ?2 and v.date < ?3")
    List<Viewing> findAllByUserGroupAndDateBefore(Long hospitalId, String username, LocalDate endDate);

    @Query("select v from Viewing v where v.userGroup.hospitalId = ?1 and v.userGroup.username = ?2 and v.date > ?3")
    List<Viewing> findAllByUserGroupAndDateAfter(Long hospitalId, String username, LocalDate startDate);

    // misc queries

    @Query("select v from  Viewing v where v.date = ?1")
    List<Viewing> getAllByDate(LocalDate date);

    @Query("select v from Viewing v where v.equipmentId.equipmentId=?1 and v.date=?2 and v.userGroup = ?3")
    Viewing getViewToUpdate(long equipmentId,LocalDate date,UserGroup group);

    @Transactional
    @Modifying
    @Query("update Viewing v set v.viewCounter = ?2,v.version = v.version+1 where v.viewingId=?1 and v.version=?3")
    void incrementCounter(String id,long counter,long version);
}
