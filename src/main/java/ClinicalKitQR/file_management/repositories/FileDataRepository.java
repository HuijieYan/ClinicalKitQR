package ClinicalKitQR.file_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ClinicalKitQR.file_management.models.FileData;

public interface FileDataRepository extends JpaRepository<FileData,String> {
}
