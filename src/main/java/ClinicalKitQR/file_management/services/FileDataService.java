package ClinicalKitQR.file_management.services;

import ClinicalKitQR.Constant;
import ClinicalKitQR.file_management.models.FileData;
import ClinicalKitQR.file_management.repositories.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileDataService {
    private final FileDataRepository repository;
    private Path root = Constant.uploadedFileRoot;

    @Autowired
    public FileDataService(FileDataRepository repository){
        this.repository = repository;
    }

    /**
     * Creates directory /uploadedFiles if it is not yet created
     */
    @PostConstruct
    public void init(){
        if (!Files.exists(root)) {
            try {
                Files.createDirectory(root);
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize folder for upload! Error:" + e.getMessage());
            }
        }
    }

    public void save(FileData fileData, MultipartFile file) throws Exception{
        Files.copy(file.getInputStream(),root.resolve(fileData.getId()));
        //copy the uploaded file to local storage
        repository.save(fileData);
    }

    public FileData get(String id){
        FileData fileData = repository.getById(id);
        return fileData;
    }

    //function getAll and delete is for testing only

    public List<FileData> getAll(){
        return repository.findAll();
    }

    public void delete(String id){
        repository.deleteById(id);
        try{
            Path path = root.resolve(id);
            Files.delete(path);
        }catch (Exception e){
        }
    }
}
