package team7.demo.equipment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team7.demo.equipment.models.SentEquipment;

import javax.transaction.Transactional;

public interface SentEquipmentRepository extends JpaRepository<SentEquipment,String> {
    @Query("select e from SentEquipment e where e.id = ?1")
    SentEquipment getById(String id);

    @Transactional
    @Modifying
    @Query("delete from SentEquipment e where e.id=?1")
    void deleteById(String id);

    @Transactional
    @Modifying
    @Query("update SentEquipment e set e.saved=true where e.id=?1")
    void updateSaved(String id);
}
