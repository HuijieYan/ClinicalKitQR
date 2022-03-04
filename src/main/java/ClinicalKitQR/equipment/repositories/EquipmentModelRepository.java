package ClinicalKitQR.equipment.repositories;

import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, String> {
}
