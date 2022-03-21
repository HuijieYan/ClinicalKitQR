package ClinicalKitQR.file_management.controllers;

import ClinicalKitQR.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ClinicalKitQR.file_management.models.FileData;
import ClinicalKitQR.file_management.services.FileDataService;
import ClinicalKitQR.login.services.UserGroupService;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import org.springframework.web.client.RestTemplate;

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
            return null;
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
    public File download(@PathVariable String id){
        try{
            RestTemplate restTemplate = new RestTemplate();
            File file = restTemplate.execute(Constant.FRONTEND_URL+"/pass/"+id, HttpMethod.GET, null, clientHttpResponse -> {
                File ret = File.createTempFile("download", "tmp");
                StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
                return ret;
            });

            return file;
        }catch (Exception e){
            return null;
        }
    }

    @GetMapping("/pass/{id}")
    public ResponseEntity<Object> passFile(@PathVariable String id){
        try {
            File file = service.get(id);


            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
            ContentDisposition contentDisposition = ContentDisposition.
                    builder("inline").filename(file.getName()).build();
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                    .body(resource);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
