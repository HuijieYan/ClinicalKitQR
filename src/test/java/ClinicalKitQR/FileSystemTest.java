package ClinicalKitQR;


import ClinicalKitQR.file_management.controllers.FileDataController;
import ClinicalKitQR.file_management.models.FileData;
import ClinicalKitQR.file_management.services.FileDataService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.TrustService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  For file system, we test FileDataController
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileSystemTest {
    @Autowired
    private FileDataController fileDataController;

    private String fileName = "test.txt";

    private MultipartFile testFile;

    @Autowired
    private TrustService trustService;

    @Autowired
    private FileDataService fileDataService;

    private UserGroup group;

    private FileData data;

    private Trust trust;

    @BeforeAll
    public void setUp(){
        try {
            File file = new File("test.txt");
            FileInputStream stream = new FileInputStream(file);
            byte[] bytes = stream.readAllBytes();
            stream.close();
            testFile = new MockMultipartFile("uploaded-file",
                    fileName,
                    "text/plain",
                    bytes
            );

            //there's a test.txt file in /test/java/ClinicalKitQR
        }catch (Exception e){
            testFile = new MockMultipartFile("uploaded-file",
                    fileName,
                    "text/plain",
                    "Testing\nThis is me".getBytes()
            );
            //if such txt file does not exist
        }
        trust = new Trust("test trust");
        Hospital hospital = new Hospital("test hospital",trust);
        trust.addHospital(hospital);
        group = new UserGroup("test group 1","g1","123",hospital,true,"","");
        hospital.addGroup(group);
        trustService.save(trust);
    }

    @Test
    @Order(1)
    public void testUpload(){
        int originalSize = fileDataService.getAll().size();
        fileDataController.upload(testFile,group.getUsername(),group.getHospitalId().getHospitalId());
        assertEquals(originalSize+1,fileDataService.getAll().size());
        data = fileDataService.getAll().get(originalSize);

        //the code below tests function when invalid entries are entered
        assertEquals("Unable to save file. Error: your login details are either invalid or expired",fileDataController.upload(testFile,"invalid user",-1));
        assertEquals("Unable to save file. Error: uploaded file is empty",fileDataController.upload(null,group.getUsername(),group.getHospitalId().getHospitalId()));
        assertEquals(originalSize+1,fileDataService.getAll().size());
    }

    @Test
    @Order(2)
    public void testDownload(){
        ResponseEntity<InputStreamResource> response = null;
        try {
            response = fileDataController.download(data.getId());
            InputStreamResource body = response.getBody();
            String content = new String(body.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("Testing\nThis is me",content);
            assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0).equals("attachment;filename="+fileName));

            assertNull(fileDataController.download("invalid id"));

            //body.getInputStream().close();
            //manually close the stream in test to delete our test file in /uploadedFiles
        }catch (Exception e){

        }
        assertNotNull(response);
    }

    @AfterAll
    public void cleanUp(){
        trustService.delete(trust.getTrustId());
        fileDataService.delete(data.getId());
    }
}
