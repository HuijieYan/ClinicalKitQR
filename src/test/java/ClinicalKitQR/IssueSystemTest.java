package ClinicalKitQR;

import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.models.Manufacturer;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.equipment.services.ManufacturerService;
import ClinicalKitQR.issue.controllers.IssueController;
import ClinicalKitQR.issue.models.Issue;
import ClinicalKitQR.issue.services.IssueService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.TrustService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IssueSystemTest {
    private Issue testIssue;

    private Trust testTrust;

    private Hospital testHospital1;

    private Hospital testHospital2;


    private UserGroup group1;

    private UserGroup group2;

    private Equipment testEquipment1;

    private Equipment testEquipment2;

    private Issue testIssue1;

    private Issue testIssue2;

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueController issueController;

    @Autowired
    private TrustService trustService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ManufacturerService manufacturerService;

    @BeforeAll
    public void preset(){
        testTrust = new Trust("test trust");
        testHospital1 = new Hospital("test hospital1",testTrust);
        testHospital2 = new Hospital("test hospital2",testTrust);
        group1 = new UserGroup("test group 1","g1","123",testHospital1,true,"","");
        group2 = new UserGroup("test group 2","g1","123",testHospital2,true,"","");
        Manufacturer manufacturer = new Manufacturer("test manufacturer");
        manufacturerService.save(manufacturer);
        testEquipment1 = new Equipment("test equipment 1","",testHospital1,"A","B",new EquipmentModel("test model",manufacturer));
        testEquipment2 = new Equipment("test equipment 2","",testHospital2,"A","B",new EquipmentModel("test model2",manufacturer));
        trustService.save(testTrust);
    }

    @Test
    @Order(1)
    public void testAddIssue(){
        issueController.addNewIssue(testHospital1.getHospitalId(), group1.getUsername(),testEquipment1.getEquipmentId(),"description");
        assertEquals(1,issueService.getAllByHospital(testHospital1.getHospitalId()).size());
        testIssue1 = issueService.getAllByHospital(testHospital1.getHospitalId()).get(0);

        //the code below tests when invalid entry is entered
        assertEquals("Error: The description entered is empty",issueController.addNewIssue(testHospital1.getHospitalId(), group1.getUsername(),testEquipment1.getEquipmentId(),""));
        assertEquals("Error: The equipment is not found",issueController.addNewIssue(testHospital1.getHospitalId(), group1.getUsername(),-1,""));
        assertEquals("Error: The user group is not found",issueController.addNewIssue(-1, "",testEquipment1.getEquipmentId(),""));
    }

    @Test
    @Order(2)
    public void testUpdateIssue(){
        //solved in issue is set to false by default
        issueController.updateSolved(testIssue1.getIssueId(), true);
        testIssue1 = issueService.getAllByHospital(testHospital1.getHospitalId()).get(0);
        assertTrue(testIssue1.isSolved());

        //because it's impossible to update a non-existing issue at frontend, this is not tested
    }

    @Test
    @Order(3)
    public void testGetByHospital(){
        issueController.addNewIssue(testHospital1.getHospitalId(), group1.getUsername(),testEquipment1.getEquipmentId(),"description");
        //add another issue to test hospital 1 for test equipment 1
        assertEquals(2,issueController.getByHospital(testHospital1.getHospitalId()).size());
        assertEquals("description",issueController.getByHospital(testHospital1.getHospitalId()).get(0).getDescription());

        List<Issue> emptyList = new ArrayList<>();
        assertEquals(emptyList,issueController.getByHospital(-1));
    }

    @Test
    @Order(4)
    public void testGetByTrust(){
        issueController.addNewIssue(testHospital2.getHospitalId(), group2.getUsername(),testEquipment2.getEquipmentId(),"description");
        assertEquals(3,issueController.getByTrust(testTrust.getTrustId()).size());

        List<Issue> emptyList = new ArrayList<>();
        assertEquals(emptyList,issueController.getByHospital(-1));
    }

    @Test
    @Order(5)
    public void testDeleteIssue(){
        issueController.deleteById(testIssue1.getIssueId());
        assertEquals(1,issueController.getByHospital(testHospital1.getHospitalId()).size());
        //only has the issue that was created at testGetByHospital

        issueController.deleteById(testIssue1.getIssueId());
        assertEquals(1,issueController.getByHospital(testHospital1.getHospitalId()).size());
        //when deleting non-existing issue, nothing happens
    }
    
    @AfterAll
    public void cleanUp(){
        trustService.delete(testTrust.getTrustId());
    }
}
