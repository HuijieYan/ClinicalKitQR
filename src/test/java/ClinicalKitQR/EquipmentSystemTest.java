package ClinicalKitQR;

import ClinicalKitQR.equipment.controllers.EquipmentController;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.services.EquipmentModelService;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.equipment.services.ManufacturerService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.TrustService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *  For equipment education system, we are testing EquipmentController
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentSystemTest {
    private Trust testTrust;

    private Hospital testHospital;

    private UserGroup testUser;

    private Equipment testEquipment;

    @Autowired
    private EquipmentController equipmentController;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentModelService equipmentModelService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private TrustService trustService;

    @BeforeAll
    public void preset(){
        testTrust = new Trust("test trust");
        testHospital = new Hospital("test hospital",testTrust);
        testTrust.addHospital(testHospital);
        testUser = new UserGroup("test group 1","g1","123",testHospital,true,"","");
        testHospital.addGroup(testUser);
        trustService.save(testTrust);
    }

    @Test
    @Order(1)
    public void testSaveEquipment(){
        int originalSize = equipmentService.getAll().size();
        equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"TEST-1","Apple");
        assertEquals(originalSize+1,equipmentService.getAll().size());
        testEquipment = equipmentService.getAll().get(originalSize);
        assertEquals("test",testEquipment.getContent());
        assertEquals("test equipment 1",testEquipment.getName());
        assertEquals("Respiratory",testEquipment.getType());
        assertEquals("Adult",testEquipment.getCategory());
        assertEquals("TEST-1",testEquipment.getModel().getModelName());
        assertEquals("Apple",testEquipment.getModel().getManufacturer().getManufacturerName());

        //the code below tests when invalid entry is entered
        assertEquals("Failed to save the equipment, error: Invalid entries",equipmentController.save("","test",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"TEST-1","Apple"));
        assertEquals("Failed to save the equipment, error: Invalid entries",equipmentController.save("test equipment 1","",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"TEST-1","Apple"));
        assertEquals("Failed to save the equipment, error: Invalid entries",equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"","Adult",testUser.getUsername(),"TEST-1","Apple"));
        assertEquals("Failed to save the equipment, error: Invalid entries",equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"Respiratory","",testUser.getUsername(),"TEST-1","Apple"));
        assertEquals("Failed to save the equipment, error: Invalid entries",equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"","Apple"));
        assertEquals("Failed to save the equipment, error: Invalid entries",equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"TEST-1",""));
    }

    @Test
    @Order(2)
    public void test(){

    }
}
