package ClinicalKitQR.equipment.repositories;

import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, String> {
    @Query("select m from EquipmentModel m where m.equipment.hospitalId.hospitalId=?1 order by m.modelName ASC")
    List<EquipmentModel> findByHospitalId(long hospitalId);

    @Query("select m from EquipmentModel m where m.equipment.hospitalId.hospitalId=?1 and m.manufacturer.manufacturerName=?2 order by m.modelName ASC")
    List<EquipmentModel> findByHospitalIdAndManufacturer(long hospitalId,String manufacture);

    @Query("select m from EquipmentModel m where m.equipment.hospitalId.trust.trustId=?1 order by m.modelName ASC")
    List<EquipmentModel> findByTrustId(long trustId);

    @Query("select m from EquipmentModel m where m.equipment.hospitalId.trust.trustId=?1 and m.manufacturer.manufacturerName=?2 order by m.modelName ASC")
    List<EquipmentModel> findByTrustIdAndManufacturer(long trustId,String manufacture);

    @Transactional
    @Modifying
    @Query("update EquipmentModel m set m.modelName =?2 where m.modelId=?1")
    void updateName(String modelId,String name);
}
