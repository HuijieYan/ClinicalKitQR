package ClinicalKitQR.equipment.repositories;

import ClinicalKitQR.equipment.models.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
    @Query("select e from Equipment e where e.equipmentId = ?1")
    Equipment findById(long id);

    @Query("select e from Equipment e where e.hospitalId.hospitalId = ?1")
    List<Equipment> findByHospital(long id);

    @Query("select e from Equipment e where e.hospitalId.trust.trustId = ?1 order by e.hospitalId.hospitalId")
    List<Equipment> findByTrust(long id);

    @Transactional
    @Modifying
    @Query("delete from Equipment e where e.equipmentId = ?1")
    void deleteById(long id);

    @Transactional
    @Modifying
    @Query("update Equipment e set e.name=?2,e.searchName=?3,e.content =?4,e.type=?5,e.category=?6  where e.equipmentId = ?1")
    void update(long id,String name, String searchName,String content,String type,String category);

    @Transactional
    @Modifying
    @Query("update Equipment e set e.equipmentId=?2 where e.equipmentId = ?1")
    void updateId(long id,long newId);

    @Query("select e from Equipment e where e.hospitalId.hospitalId=?1 and (?2 = '' or e.type=?2) and (?3 = '' or e.category=?3)" +
            " and (?4 = '' or e.searchName like %?4% or e.model.modelSearchName like %?4%) and " +
            "(?5 = '' or e.model.manufacturer.manufacturerName = ?5) and (?6 = '' or e.model.modelName = ?6)")
    List<Equipment> searchByHospital(long id,String type, String category,String text,String manufacturer,String model);

    @Query("select e from Equipment e where e.hospitalId.trust.trustId=?1 and (?2 = '' or e.type=?2) and (?3 = '' or e.category=?3)" +
            " and (?4 = '' or e.searchName like %?4% or e.model.modelSearchName like %?4%) and " +
            "(?5 = '' or e.model.manufacturer.manufacturerName = ?5) and (?6 = '' or e.model.modelName = ?6)")
    List<Equipment> searchByTrust(long id,String type, String category,String text,String manufacturer,String model);

    @Query(value="SELECT * FROM equipment ORDER BY equipment_id DESC LIMIT 1", nativeQuery = true)
    Equipment findTop();
}
