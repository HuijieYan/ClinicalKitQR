package team7.demo.equipment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team7.demo.equipment.models.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
    @Query("select e from Equipment e where e.equipmentId = ?1")
    public Equipment findById(long id);
}
