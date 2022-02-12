package team7.demo.equipment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team7.demo.equipment.models.Equipment;

import javax.transaction.Transactional;
import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
    @Query("select e from Equipment e where e.equipmentId = ?1")
    public Equipment findById(long id);

    @Query("select e from Equipment e where e.hospitalId.hospitalId = ?1")
    public List<Equipment> findByHospital(long id);

    @Query("select e from Equipment e where e.hospitalId.trust.trustId = ?1")
    public List<Equipment> findByTrust(long id);

    @Transactional
    @Modifying
    @Query("delete from Equipment e where e.equipmentId = ?1")
    public void deleteById(long id);

    @Transactional
    @Modifying
    @Query("update Equipment e set e.name=?2,e.searchName=?3,e.content =?4,e.type=?5,e.category=?6  where e.equipmentId = ?1")
    public void update(long id,String name, String searchName,String content,String type,String category);

    @Query("select e from Equipment e where e.hospitalId.hospitalId=?1 and e.type=?2 and e.category=?3 and e.searchName like %?4% ")
    public List<Equipment> findByCategoryAndTypeAndNameAndHospital(long id,String type, String category,String text);

    @Query("select e from Equipment e where e.hospitalId.hospitalId=?1 and e.category=?2 and e.searchName like %?3%")
    public List<Equipment> findByCategoryAndNameAndHospital(long id, String category,String text);

    @Query("select e from Equipment e where e.hospitalId.hospitalId=?1 and e.type=?2 and e.searchName like %?3% ")
    public List<Equipment> findByTypeAndNameAndHospital(long id, String type,String text);

    @Query("select e from Equipment e where e.hospitalId.hospitalId=?1 and e.searchName like %?2% ")
    public List<Equipment> findByNameAndHospital(long id,String text);

    @Query("select e from Equipment e where e.hospitalId.trust.trustId=?1 and e.type=?2 and e.category=?3 and e.searchName like %?4%")
    public List<Equipment> findByCategoryAndTypeAndNameAndTrust(long id,String type, String category,String text);

    @Query("select e from Equipment e where e.hospitalId.trust.trustId=?1 and e.category=?2 and e.searchName like %?3%")
    public List<Equipment> findByCategoryAndNameAndTrust(long id, String category,String text);

    @Query("select e from Equipment e where e.hospitalId.trust.trustId=?1 and e.type=?2 and e.searchName like %?3%")
    public List<Equipment> findByTypeAndNameAndTrust(long id, String type,String text);

    @Query("select e from Equipment e where e.hospitalId.trust.trustId=?1 and e.searchName like %?2%")
    public List<Equipment> findByNameAndTrust(long id, String text);
}
