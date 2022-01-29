package team7.demo.file_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team7.demo.file_management.models.FileData;
import team7.demo.file_management.services.FileDataService;

import java.io.File;
import java.nio.file.Files;

@CrossOrigin(origins = {"http://localhost:3000/"})
@RestController
@RequestMapping("/file")
public class FileDataController {
    private final FileDataService service;

    @Autowired
    public FileDataController(FileDataService service){
        this.service = service;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile file){
        String name = file.getOriginalFilename();
        int index = name.lastIndexOf('.');
        System.out.println(name);
        FileData data = new FileData(name.substring(0,index),name.substring(index));
        try{
            service.save(data,file);
            System.out.println(data.getPath());
            return "{ \"location\": \""+""+data.getPath()+"\" }";
        }catch (Exception e){
            return "Unable to save file. Error:"+e.getMessage();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id){
        try{
            File file = service.get(id);
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        }catch (Exception e){
            return null;
        }
    }
}
