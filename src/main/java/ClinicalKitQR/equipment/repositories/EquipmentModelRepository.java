package ClinicalKitQR.equipment.repositories;

import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, String> {
    @Query("select m from EquipmentModel m where m.equipment.hospitalId.hospitalId=?1")
    List<EquipmentModel> findByHospitalId(long hospitalId);

    @Query("select m from EquipmentModel m where m.equipment.hospitalId.hospitalId=?1 and m.manufacturer.manufacturerName=?2")
    List<EquipmentModel> findByHospitalIdAndManufacturer(long hospitalId,String manufacture);

    @Query("select m from EquipmentModel m where m.equipment.hospitalId.trust.trustId=?1")
    List<EquipmentModel> findByTrustId(long trustId);

    @Query("select m from EquipmentModel m where m.equipment.hospitalId.trust.trustId=?1 and m.manufacturer.manufacturerName=?2")
    List<EquipmentModel> findByTrustIdAndManufacturer(long trustId,String manufacture);
}
