package ClinicalKitQR.file_management.services;

import ClinicalKitQR.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ClinicalKitQR.file_management.models.FileData;
import ClinicalKitQR.file_management.repositories.FileDataRepository;

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

    public void save(FileData fileData, BufferedImage img) throws Exception{
        File file = new File(fileData.getId());
        ImageIO.write(img,"png",file);
        Files.copy(file.toPath(),root.resolve(fileData.getId()));
        //copy the uploaded file to local storage
        repository.save(fileData);
    }

    public File get(String id) throws Exception{
        FileData fileData = repository.getById(id);
        return new File(root.toAbsolutePath()+"/"+fileData.getId());
    }
}
