package ClinicalKitQR.file_management.repositories;

import ClinicalKitQR.file_management.models.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileDataRepository extends JpaRepository<FileData,String> {
    @Query("select f from FileData f where f.id=?1")
    FileData getById(String id);
}
