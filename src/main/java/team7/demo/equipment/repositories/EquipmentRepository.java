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
}
