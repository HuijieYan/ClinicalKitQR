package ClinicalKitQR.equipment.repositories;

import ClinicalKitQR.equipment.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer,String> {
    @Query("select m from Manufacturer m where m.manufacturerName = ?1")
    Manufacturer findByName(String name);

    @Query("select m from Manufacturer m order by m.manufacturerName ASC ")
    List<Manufacturer> getAll();

    @Transactional
    @Modifying
    @Query("delete from Manufacturer m where m.manufacturerName = ?1")
    void deleteByName(String name);
}
