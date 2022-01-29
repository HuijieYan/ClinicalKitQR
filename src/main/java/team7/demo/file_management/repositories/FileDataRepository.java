package team7.demo.file_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.demo.file_management.models.FileData;

public interface FileDataRepository extends JpaRepository<FileData,String> {
}
