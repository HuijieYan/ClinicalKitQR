package ClinicalKitQR;

import ClinicalKitQR.equipment.controllers.EquipmentController;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.models.Manufacturer;
import ClinicalKitQR.equipment.services.EquipmentModelService;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.equipment.services.ManufacturerService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.TrustService;
import ClinicalKitQR.viewing.controllers.ViewingController;
import ClinicalKitQR.viewing.models.EquipmentViewing;
import ClinicalKitQR.viewing.services.ViewingService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  For equipment education system, we are testing EquipmentController and ViewingController
 *
 *  Unfortunately we cannot test getEquipmentQRCode from EquipmentController since it is returning an image
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentSystemTest {
    private Trust testTrust;

    private Hospital testHospital;

    private Hospital testHospital2;

    private UserGroup testUser;

    private UserGroup testUserFromOtherHospital;

    private UserGroup testNormalUser;

    private UserGroup testNormalUser2;

    private UserGroup trustAdmin;

    private Equipment testEquipment;

    @Autowired
    private EquipmentController equipmentController;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private TrustService trustService;

    @Autowired
    private ViewingController viewingController;

    @Autowired
    private ViewingService viewingService;

    @BeforeAll
    public void preset(){
        testTrust = new Trust("test trust");
        testHospital = new Hospital("test hospital",testTrust);
        testHospital2 = new Hospital("test hospital2",testTrust);
        testTrust.addHospital(testHospital);
        testTrust.addHospital(testHospital2);
        testUser = new UserGroup("test group 1","g1","123",testHospital,true,"","");
        testNormalUser = new UserGroup("test group 2","g2","123",testHospital,false,"","");
        testNormalUser2 = new UserGroup("test group 3","g3","123",testHospital,false,"","");
        trustAdmin = new UserGroup("admin","g1","123",testTrust.getHospitals().get(0),true,"","");
        testTrust.getHospitals().get(0).addGroup(trustAdmin);
        testHospital.addGroup(testUser);
        testHospital.addGroup(testNormalUser);
        testHospital.addGroup(testNormalUser2);
        testUserFromOtherHospital = new UserGroup("test group 2","g2","123",testHospital2,true,"","");
        testHospital2.addGroup(testUserFromOtherHospital);
        trustService.save(testTrust);
    }

    @Test
    @Order(1)
    public void testSaveEquipmentAndGetManufacturers(){
        int expectManufacturerSize = manufacturerService.getAll().size();
        if(manufacturerService.getByName("Test Manufacturer") == null){
            expectManufacturerSize = equipmentController.getAllManufacturers(testHospital.getHospitalId(),testUser.getUsername()).length+1;
        }
        //we assume that the manufacturer called
        int originalSize = equipmentService.getAll().size();
        equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"TEST-1","Test Manufacturer");
        assertEquals(originalSize+1,equipmentService.getAll().size());
        assertEquals(expectManufacturerSize,manufacturerService.getAll().size());
        testEquipment = equipmentService.getAll().get(originalSize);
        assertEquals("test",testEquipment.getContent());
        assertEquals("test equipment 1",testEquipment.getName());
        assertEquals("Respiratory",testEquipment.getType());
        assertEquals("Adult",testEquipment.getCategory());
        assertEquals("TEST-1",testEquipment.getModel().getModelName());
        assertEquals("Test Manufacturer",testEquipment.getModel().getManufacturer().getManufacturerName());

        //the code below tests when invalid entry is entered
        String failString = "Failed to save the equipment, error: Invalid entries";
        assertEquals(failString,equipmentController.save("","test",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"TEST-1","Test Manufacturer"));
        assertEquals(failString,equipmentController.save("test equipment 1","",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"TEST-1","Test Manufacturer"));
        assertEquals(failString,equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"","Adult",testUser.getUsername(),"TEST-1","Test Manufacturer"));
        assertEquals(failString,equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"Respiratory","",testUser.getUsername(),"TEST-1","Test Manufacturer"));
        assertEquals(failString,equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"","Test Manufacturer"));
        assertEquals(failString,equipmentController.save("test equipment 1","test",testHospital.getHospitalId(),"Respiratory","Adult",testUser.getUsername(),"TEST-1",""));
        assertEquals("Failed to save the equipment, error: User does not exist or login session expired",equipmentController.save("test equipment 1","test",-1,"Respiratory","Adult","invalid user","TEST-1","Test Manufacturer"));
        assertEquals(expectManufacturerSize,manufacturerService.getAll().size());
    }

    @Test
    @Order(2)
    public void testUpdateEquipment(){
        equipmentController.update("updated equipment","updated",testHospital.getHospitalId(),
                "Gastrointestinal","Neonatal",testUser.getUsername(),testEquipment.getEquipmentId(),
                "TEST-2","Test Manufacturer 2");
        testEquipment = equipmentService.getAll().get(equipmentService.getAll().size()-1);
        assertEquals("updated",testEquipment.getContent());
        assertEquals("updated equipment",testEquipment.getName());
        assertEquals("Gastrointestinal",testEquipment.getType());
        assertEquals("Neonatal",testEquipment.getCategory());
        assertEquals("TEST-2",testEquipment.getModel().getModelName());
        assertEquals("Test Manufacturer 2",testEquipment.getModel().getManufacturer().getManufacturerName());

        equipmentController.update("updated equipment","updated",testHospital.getHospitalId(),
                "Gastrointestinal","Neonatal",testUser.getUsername(),testEquipment.getEquipmentId(),
                "TEST-4","Test Manufacturer");
        testEquipment = equipmentService.getAll().get(equipmentService.getAll().size()-1);
        assertEquals("updated",testEquipment.getContent());
        assertEquals("updated equipment",testEquipment.getName());
        assertEquals("Gastrointestinal",testEquipment.getType());
        assertEquals("Neonatal",testEquipment.getCategory());
        assertEquals("TEST-4",testEquipment.getModel().getModelName());
        assertEquals("Test Manufacturer",testEquipment.getModel().getManufacturer().getManufacturerName());

        //the code below tests when invalid entry is entered
        String failString = "Failed to update the equipment, error: Invalid entries";
        assertEquals(failString,equipmentController.update("","updated",testHospital.getHospitalId(),
                "Gastrointestinal","Neonatal",testUser.getUsername(),testEquipment.getEquipmentId(),
                "TEST-2","Test Manufacturer 2"));
        assertEquals(failString,equipmentController.update("updated equipment","",testHospital.getHospitalId(),
                "Gastrointestinal","Neonatal",testUser.getUsername(),testEquipment.getEquipmentId(),
                "TEST-2","Test Manufacturer 2"));
        assertEquals(failString,equipmentController.update("updated equipment","updated",testHospital.getHospitalId(),
                "","Neonatal",testUser.getUsername(),testEquipment.getEquipmentId(),
                "TEST-2","Test Manufacturer 2"));
        assertEquals(failString,equipmentController.update("updated equipment","updated",testHospital.getHospitalId(),
                "Gastrointestinal","",testUser.getUsername(),testEquipment.getEquipmentId(),
                "TEST-2","Test Manufacturer 2"));
        assertEquals(failString,equipmentController.update("updated equipment","updated",testHospital.getHospitalId(),
                "Gastrointestinal","Neonatal",testUser.getUsername(),testEquipment.getEquipmentId(),
                "","Test Manufacturer 2"));
        assertEquals(failString,equipmentController.update("updated equipment","updated",testHospital.getHospitalId(),
                "Gastrointestinal","Neonatal",testUser.getUsername(),testEquipment.getEquipmentId(),
                "TEST-2",""));
        assertEquals("Failed to update the equipment, error: User does not exist or login session expired",equipmentController.update("updated equipment","updated",-1,
                "Gastrointestinal","Neonatal","invalid user",testEquipment.getEquipmentId(),
                "TEST-2","Test Manufacturer 2"));
        assertEquals("Failed to update the equipment, error: Equipment not found",equipmentController.update("updated equipment","updated",testHospital.getHospitalId(),
                "Gastrointestinal","Neonatal",testUser.getUsername(),-1,
                "TEST-2","Test Manufacturer 2"));
        assertEquals("Failed to update the equipment, error: Your user group has no right to edit this equipment",equipmentController.update("updated equipment","updated",testHospital2.getHospitalId(),
                "Gastrointestinal","Neonatal",testUserFromOtherHospital.getUsername(),testEquipment.getEquipmentId(),
                "TEST-2","Test Manufacturer 2"));
    }

    @Test
    @Order(3)
    public void testGetEquipmentById(){
        equipmentController.getById(testEquipment.getEquipmentId(),testUser.getHospitalId().getHospitalId(),testUser.getUsername());
        assertEquals("updated",testEquipment.getContent());
        assertEquals("updated equipment",testEquipment.getName());
        assertEquals("Gastrointestinal",testEquipment.getType());
        assertEquals("Neonatal",testEquipment.getCategory());
        assertEquals("TEST-4",testEquipment.getModel().getModelName());
        assertEquals("Test Manufacturer",testEquipment.getModel().getManufacturer().getManufacturerName());

        //the code below tests when invalid entry is entered
        assertNull(equipmentController.getById(-1,testUser.getHospitalId().getHospitalId(),testUser.getUsername()));
        assertNull(equipmentController.getById(testEquipment.getEquipmentId(),-1,"invalid user"));
        assertNull(equipmentController.getById(testEquipment.getEquipmentId(),testUserFromOtherHospital.getHospitalId().getHospitalId(),testUserFromOtherHospital.getUsername()));
    }

    @Test
    @Order(4)
    public void testGetViewingsByEquipmentAndDates(){
        //we will use 2 normal user groups to test this part,
        //this will prove that our system is capable of handling
        //view record of multiple user groups and across multiple
        //equipments
        viewingService.addNewView(testEquipment, LocalDate.of(2022,2,22),testNormalUser);
        viewingService.addNewView(testEquipment, LocalDate.of(2022,2,22),testNormalUser);
        viewingService.addNewView(testEquipment, LocalDate.of(2022,2,21),testNormalUser);
        viewingService.addNewView(testEquipment, LocalDate.of(2022,3,26),testNormalUser);
        //normal user 1 viewed this equipment twice on 22/2/2022, once on 21/22/2022
        viewingService.addNewView(testEquipment,LocalDate.of(2022,2,22),testNormalUser2);
        viewingService.addNewView(testEquipment,LocalDate.of(2022,3,30),testNormalUser2);
        List<EquipmentViewing> result = viewingController.getByEquipmentAndDateBetween(testEquipment.getEquipmentId(),"2022-02-10T04:09:13Z","2022-03-23T16:50:59Z");
        int user1Index = 0;
        int user2Index = 0;
        if(result.get(0).getUserGroup().getUsername().equals(testNormalUser.getUsername())){
            user2Index = 1;
        }else{
            user1Index = 1;
            user2Index = 0;
        }

        assertEquals(3,result.get(user1Index).getViewCount());
        assertEquals(1,result.get(user2Index).getViewCount());
    }


    /**
     * Name                 Model           Hospital               Manufacturer
     * test equipment 2     TEST-1          test hospital 1        Test Manufacturer
     * test equipment 7     TEST-2          test hospital 2        Test Manufacturer 2
     * my equipment 4       TEST-3          test hospital 2        Test Manufacturer
     * updated equipment    TEST-4          test hospital 1        Test Manufacturer
     * test equipment 3     TEST-5          test hospital 2        Test Manufacturer 2
     * test equipment 5     TEST-6          test hospital 1        Test Manufacturer 2
     */
    @Test
    @Order(5)
    public void testGetModelsByManufacturer(){
        Manufacturer testManufacturer = manufacturerService.getByName("Test Manufacturer");
        Equipment equipment = new Equipment("test equipment 2","",testHospital,"Respiratory","Adult",new EquipmentModel("TEST-1",testManufacturer));
        equipmentService.save(equipment);
        equipment = new Equipment("test equipment 3","",testHospital2,"Haematological","Neonatal",new EquipmentModel("TEST-5",manufacturerService.getByName("Test Manufacturer 2")));
        equipmentService.save(equipment);
        equipment = new Equipment("my equipment 4","",testHospital2,"Neurological","Children",new EquipmentModel("TEST-3",testManufacturer));
        equipmentService.save(equipment);

        List<String> names = Arrays.asList(equipmentController.getModelsByManufacturer(testUser.getHospitalId().getHospitalId(),testUser.getUsername(),testManufacturer.getManufacturerName()));
        assertTrue(names.contains("TEST-1"));
        assertTrue(names.contains("TEST-4"));
        //when a hospital admin using this function

        names = Arrays.asList(equipmentController.getModelsByManufacturer(trustAdmin.getHospitalId().getHospitalId(),trustAdmin.getUsername(),testManufacturer.getManufacturerName()));
        assertTrue(names.contains("TEST-1"));
        assertTrue(names.contains("TEST-3"));
        assertTrue(names.contains("TEST-4"));
        //when a trust admin using this function

        manufacturerService.save(new Manufacturer("Test Manufacturer 3"));
        assertEquals(0,equipmentController.getModelsByManufacturer(testUser.getHospitalId().getHospitalId(),testUser.getUsername(),"Test Manufacturer 3").length);

        //the code below tests when invalid entry is entered
        assertEquals(0,equipmentController.getModelsByManufacturer(-1,"invalid user",testManufacturer.getManufacturerName()).length);
        assertEquals(0,equipmentController.getModelsByManufacturer(testUser.getHospitalId().getHospitalId(),testUser.getUsername(),"").length);
    }

    @Test
    @Order(6)
    public void testGetModelsByUser(){
        Equipment equipment = new Equipment("test equipment 5","",testHospital,"Metabolic","Children",new EquipmentModel("TEST-6",manufacturerService.getByName("Test Manufacturer 2")));
        equipmentService.save(equipment);
        equipment = new Equipment("test equipment 7","",testHospital2,"Infection/Immunity","Neonatal",new EquipmentModel("TEST-2",manufacturerService.getByName("Test Manufacturer 2")));
        equipmentService.save(equipment);

        List<String> names = Arrays.asList(equipmentController.getModelsByUser(testUser.getHospitalId().getHospitalId(),testUser.getUsername()));
        assertTrue(names.contains("TEST-1"));
        assertTrue(names.contains("TEST-4"));
        assertTrue(names.contains("TEST-6"));
        assertEquals(3,names.size());
        //when a hospital admin using this function

        names = Arrays.asList(equipmentController.getModelsByUser(trustAdmin.getHospitalId().getHospitalId(),trustAdmin.getUsername()));
        assertTrue(names.contains("TEST-1"));
        assertTrue(names.contains("TEST-2"));
        assertTrue(names.contains("TEST-3"));
        assertTrue(names.contains("TEST-4"));
        assertTrue(names.contains("TEST-5"));
        assertTrue(names.contains("TEST-6"));
        assertEquals(6,names.size());
        //when a trust admin using this function

        //the code below tests when invalid entry is entered
        assertEquals(0,equipmentController.getModelsByUser(-1,"invalid user").length);
    }

    @Test
    @Order(7)
    public void testGetAllManufacturers(){
        int originalSize = equipmentController.getAllManufacturers(testUser.getHospitalId().getHospitalId(),testUser.getUsername()).length;
        manufacturerService.save(new Manufacturer("Test Manufacturer 4"));
        assertEquals(originalSize+1,equipmentController.getAllManufacturers(testUser.getHospitalId().getHospitalId(),testUser.getUsername()).length);

        assertEquals(0,equipmentController.getAllManufacturers(-1,"invalid user").length);
    }

    @Test
    @Order(8)
    public void testGetEquipmentInHospital(){
        assertEquals(3,equipmentController.getEquipmentsInHospital(testHospital.getHospitalId()).size());
        //at this point test hospital shall have TEST-1,TEST-4 and TEST-6
        assertEquals(0,equipmentController.getEquipmentsInHospital(-1).size());
    }

    @Test
    @Order(9)
    public void testGetEquipmentInTrust(){
        assertEquals(6,equipmentController.getEquipmentsInTrust(testHospital.getTrust().getTrustId()).size());
        //at this point test hospital shall have TEST-1,TEST-2, TEST-3, TEST-4, TEST-5 and TEST-6
        assertEquals(0,equipmentController.getEquipmentsInTrust(-1).size());
    }

    /**
     * Name                 Model           Hospital               Manufacturer                Type                 Category
     * test equipment 2     TEST-1          test hospital 1        Test Manufacturer           Respiratory           Adult
     * test equipment 7     TEST-2          test hospital 2        Test Manufacturer 2         Infection/Immunity   Neonatal
     * my equipment 4       TEST-3          test hospital 2        Test Manufacturer           Neurological         Children
     * updated equipment    TEST-4          test hospital 1        Test Manufacturer           Gastrointestinal     Neonatal
     * test equipment 3     TEST-5          test hospital 2        Test Manufacturer 2         Haematological       Neonatal
     * test equipment 5     TEST-6          test hospital 1        Test Manufacturer 2         Metabolic            Children
     * test equipment 8     TEST-7          test hospital 2        Test Manufacturer           Respiratory          Children
     * test equipment 9     TEST-8          test hospital 2        Test Manufacturer 2         Respiratory          Children
     */
    @Test
    @Order(10)
    public void testSearchEquipment(){
        Equipment equipment = new Equipment("test equipment 8","",testHospital2,"Respiratory","Children",new EquipmentModel("TEST-7",manufacturerService.getByName("Test Manufacturer")));
        equipmentService.save(equipment);
        equipment = new Equipment("test equipment 9","",testHospital2,"Respiratory","Children",new EquipmentModel("TEST-8",manufacturerService.getByName("Test Manufacturer 2")));
        equipmentService.save(equipment);

        //when a hospital admin or normal user use search function in test hospital 1
        List<Equipment> equipments = equipmentController.search(testHospital.getHospitalId(),testUser.getUsername(),"","","","","test equipment");
        assertEquals(2,equipments.size());
        assertTrue(equipments.get(0).getName().equals("test equipment 2")||equipments.get(0).getName().equals("test equipment 5"));
        assertTrue(equipments.get(1).getName().equals("test equipment 2")||equipments.get(1).getName().equals("test equipment 5"));

        equipments = equipmentController.search(testHospital.getHospitalId(),testUser.getUsername(),"Gastrointestinal","","","","");
        assertEquals(1,equipments.size());
        assertTrue(equipments.get(0).getName().equals("updated equipment"));

        equipments = equipmentController.search(testHospital.getHospitalId(),testUser.getUsername(),"","Adult","","","");
        assertEquals(1,equipments.size());
        assertTrue(equipments.get(0).getName().equals("test equipment 2"));

        equipments = equipmentController.search(testHospital.getHospitalId(),testUser.getUsername(),"","","Test Manufacturer","","");
        assertEquals(2,equipments.size());
        assertTrue(equipments.get(0).getName().equals("test equipment 2")||equipments.get(0).getName().equals("updated equipment"));
        assertTrue(equipments.get(1).getName().equals("test equipment 2")||equipments.get(1).getName().equals("updated equipment"));

        equipments = equipmentController.search(testHospital.getHospitalId(),testUser.getUsername(),"","","","TEST-4","");
        assertEquals(1,equipments.size());
        assertTrue(equipments.get(0).getName().equals("updated equipment"));

        long id = trustAdmin.getHospitalId().getHospitalId();
        //when a trust admin or normal user use search function in test trust
        equipments = equipmentController.search(id,trustAdmin.getUsername(),"","","","","test equipment");
        assertEquals(6,equipments.size());
        //since the integrity of data is tested in hospital admin's test, we will only test the length of the result

        equipments = equipmentController.search(id,trustAdmin.getUsername(),"Respiratory","","","","");
        assertEquals(3,equipments.size());

        equipments = equipmentController.search(id,trustAdmin.getUsername(),"","Children","","","");
        assertEquals(4,equipments.size());

        equipments = equipmentController.search(id,trustAdmin.getUsername(),"","","Test Manufacturer","","");
        assertEquals(4,equipments.size());

        equipments = equipmentController.search(id,trustAdmin.getUsername(),"","","","TEST-7","");
        assertEquals(1,equipments.size());

        //we also test when multiple conditions are applied to a search
        equipments = equipmentController.search(id,trustAdmin.getUsername(),"Respiratory","Children","Test Manufacturer","TEST-7","test equipment 8");
        assertEquals(1,equipments.size());

        equipments = equipmentController.search(id,trustAdmin.getUsername(),"Respiratory","Children","","","");
        assertEquals(2,equipments.size());

        equipments = equipmentController.search(id,trustAdmin.getUsername(),"Respiratory","Children","Test Manufacturer 2","","");
        assertEquals(1,equipments.size());

        equipments = equipmentController.search(id,trustAdmin.getUsername(),"Respiratory","","Test Manufacturer","","");
        assertEquals(2,equipments.size());

        //the code below tests when invalid entry is entered
        equipments = equipmentController.search(-1,"invalid user","","","","TEST-4","");
        assertEquals(0,equipments.size());
        equipments = equipmentController.search(id,trustAdmin.getUsername(),"Respiratory","Children","Test Manufacturer","TEST-7","test equipment 0");
        assertEquals(0,equipments.size());
    }

    @Test
    @Order(11)
    public void testDeleteEquipment(){
        int originalSize = equipmentService.getAll().size();
        equipmentController.deleteById(testEquipment.getEquipmentId());
        assertEquals(originalSize-1,equipmentService.getAll().size());

        //the code below tests when invalid entry is entered
        equipmentController.deleteById(-1);
        assertEquals(originalSize-1,equipmentService.getAll().size());
    }

    @AfterAll
    public void cleanUp(){
        trustService.delete(testTrust.getTrustId());
    }
}
