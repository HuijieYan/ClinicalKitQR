package team7.demo.file_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team7.demo.Constant;
import team7.demo.file_management.models.FileData;
import team7.demo.file_management.repositories.FileDataRepository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileDataService {
    private final FileDataRepository repository;
    private Path root = Constant.uploadedFileRoot;

    @Autowired
    public FileDataService(FileDataRepository repository){
        this.repository = repository;
    }

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

    public File get(String id) throws Exception{
        FileData fileData = repository.getById(id);
        return new File(root.toAbsolutePath()+"\\"+fileData.getId());
    }
}
