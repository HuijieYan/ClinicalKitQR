package team7.demo.viewing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team7.demo.login.models.UserGroup;
import team7.demo.viewing.models.Viewing;

import java.time.LocalDate;
import java.util.List;

public interface ViewingRepository extends JpaRepository<Viewing, Long> {

    // equipment only queries

    @Query("select v from Viewing v where v.equipmentId.equipmentId = ?1")
    public List<Viewing> getAllByEquipmentId(Long id);

    @Query("select v from Viewing v where v.equipmentId.equipmentId = ?1 and v.date between ?2 and ?3")
    public List<Viewing> findAllByEquipmentIdAndDateBetween(Long id, LocalDate startDate, LocalDate endDate);

    @Query("select v from Viewing v where v.equipmentId.equipmentId = ?1 and v.date < ?2")
    public List<Viewing> findAllByEquipmentIdAndDateBefore(Long id, LocalDate endDate);

    @Query("select v from Viewing v where v.equipmentId.equipmentId = ?1 and v.date > ?2")
    public List<Viewing> findAllByEquipmentIdAndDateAfter(Long id, LocalDate startDate);

    // user group only queries

    @Query("select v from Viewing v where v.userGroup.hospitalId = ?1 and v.userGroup.username = ?2")
    public List<Viewing> getAllByUserGroup(Long hospitalId, String username);

    @Query("select v from Viewing v where v.userGroup.hospitalId = ?1 and v.userGroup.username = ?2 and v.date between ?3 and ?4")
    public List<Viewing> findAllByUserGroupAndDateBetween(Long hospitalId, String username, LocalDate startDate, LocalDate endDate);

    @Query("select v from Viewing v where v.userGroup.hospitalId = ?1 and v.userGroup.username = ?2 and v.date < ?3")
    public List<Viewing> findAllByUserGroupAndDateBefore(Long hospitalId, String username, LocalDate endDate);

    @Query("select v from Viewing v where v.userGroup.hospitalId = ?1 and v.userGroup.username = ?2 and v.date > ?3")
    public List<Viewing> findAllByUserGroupAndDateAfter(Long hospitalId, String username, LocalDate startDate);

    // misc queries

    @Query("select v from  Viewing v where v.date = ?1")
    public List<Viewing> getAllByDate(LocalDate date);

}
