package ClinicalKitQR.file_management.controllers;

import ClinicalKitQR.Constant;
import ClinicalKitQR.file_management.models.FileData;
import ClinicalKitQR.file_management.services.FileDataService;
import ClinicalKitQR.login.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
@RestController
@RequestMapping(Constant.API_PREFIX+"/file")
public class FileDataController {
    private final FileDataService service;
    private final UserGroupService userGroupService;

    @Autowired
    public FileDataController(FileDataService service,UserGroupService userGroupService){
        this.service = service;
        this.userGroupService = userGroupService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile file,@RequestParam("username") String username,@RequestParam("hospitalId") long hospitalId){
        if (userGroupService.findByPK(hospitalId,username) == null){
            return "Unable to save file. Error: your login details are either invalid or expired";
        }
        if(file == null||file.isEmpty()){
            return "Unable to save file. Error: uploaded file is empty";
        }
        String name = file.getOriginalFilename();
        int index = name.lastIndexOf('.');
        FileData data = new FileData(name.substring(0,index),name.substring(index));
        try{
            service.save(data,file);
            return data.getId();
        }catch (Exception e){
            return "Unable to save file. Error:"+e.getMessage();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String id) throws Exception{
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        FileData fileData = service.get(id);
        if(fileData==null){
            return null;
        }
        File file = new File(Constant.uploadedFileRoot.toAbsolutePath()+"/"+fileData.getId());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileData.getName()+fileData.getExtension())
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }
}
