package ClinicalKitQR.equipment.repositories;

import ClinicalKitQR.equipment.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer,String> {
    @Query("select m from Manufacturer m where m.manufacturerName = ?1")
    Manufacturer findByName(String name);

    @Query("select m from Manufacturer m order by m.manufacturerName ASC ")
    List<Manufacturer> getAll();
}
