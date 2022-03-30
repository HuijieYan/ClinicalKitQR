package ClinicalKitQR;

import ClinicalKitQR.equipment.controllers.SentEquipmentController;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.models.Manufacturer;
import ClinicalKitQR.equipment.models.SentEquipment;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.equipment.services.ManufacturerService;
import ClinicalKitQR.equipment.services.SentEquipmentService;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.HospitalService;
import ClinicalKitQR.login.services.TrustService;
import ClinicalKitQR.mail.controllers.MailController;
import ClinicalKitQR.mail.models.Mail;
import ClinicalKitQR.mail.services.MailService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *  For mail system, we are testing MailController and SentEquipmentController
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MailSystemTest {

    private Trust testTrust;

    private Hospital testHospital1;

    private Hospital testHospital2;

    private Hospital testHospital3;

    private UserGroup admin1;

    private UserGroup admin2;

    private UserGroup admin3;
    //admins belong to respective hospitals

    private Equipment testEquipment1InH1;

    private Equipment testEquipment2InH1;

    private Equipment testEquipment1InH2;

    private Equipment testEquipment1InH3;

    private Mail testMail1;

    @Autowired
    private MailController mailController;

    @Autowired
    private MailService mailService;

    @Autowired
    private SentEquipmentController sentEquipmentController;

    @Autowired
    private SentEquipmentService sentEquipmentService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private TrustService trustService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private HospitalService hospitalService;

    @BeforeAll
    public void preset(){
        testTrust = new Trust("Test trust");
        testHospital1 = new Hospital("Test Hospital 1",testTrust);
        testHospital2 = new Hospital("Test Hospital 2",testTrust);
        testHospital3 = new Hospital("Test Hospital 3",testTrust);
        testTrust.addHospital(testHospital1);
        testTrust.addHospital(testHospital2);
        testTrust.addHospital(testHospital3);

        Manufacturer manufacturer = new Manufacturer("test manufacturer");
        manufacturerService.save(manufacturer);
        testEquipment1InH1 = new Equipment("test equipment 1","",testHospital1,"A","B",new EquipmentModel("test model",manufacturer));
        testEquipment2InH1 = new Equipment("test equipment 2","",testHospital1,"A","B",new EquipmentModel("test model 2",manufacturer));
        testEquipment1InH2 = new Equipment("test equipment 1","",testHospital2,"A","B",new EquipmentModel("test model 3",manufacturer));
        testEquipment1InH3 = new Equipment("test equipment 1","",testHospital3,"A","B",new EquipmentModel("test model 4",manufacturer));

        admin1 = new UserGroup("test admin 1","g1","123",testHospital1,true,"","");
        testHospital1.addGroup(admin1);
        admin2 = new UserGroup("test admin 2","g2","123",testHospital2,true,"","");
        testHospital2.addGroup(admin2);
        admin3 = new UserGroup("test admin 3","g3","123",testHospital3,true,"","");
        testHospital3.addGroup(admin3);
        trustService.save(testTrust);
    }

    @Test
    @Order(1)
    public void testSendMails(){
        List<String> receiverInfo = new ArrayList<>();
        receiverInfo.add(Long.toString(admin2.getHospitalId().getHospitalId()));
        receiverInfo.add(admin2.getUsername());
        receiverInfo.add(Long.toString(admin3.getHospitalId().getHospitalId()));
        receiverInfo.add(admin3.getUsername());

        String time = "Sun, 20 Mar 2022 09:49:22 GMT";
        //a time string in RFC 1123 format

        List<String> equipmentIds = new ArrayList<>();
        equipmentIds.add(Long.toString(testEquipment1InH1.getEquipmentId()));
        equipmentIds.add(Long.toString(testEquipment2InH1.getEquipmentId()));

        int originalSize = mailService.getAll().size();
        int originalSentEquipmentSize = sentEquipmentService.getAll().size();
        mailController.send(admin1.getHospitalId().getHospitalId(),admin1.getUsername(),receiverInfo,"test mail","test",time,equipmentIds);
        //test sending a sharing from admin 1 to admin 2 and 3 with multiple equipments shared
        assertEquals(originalSentEquipmentSize+6,sentEquipmentService.getAll().size());
        assertEquals(originalSize+3,mailService.getAll().size());
        //when sent successful, this will creat 3 mail objects, all of them have same
        //content(different sent equipments object but they are essentially the same
        // in terms of content). Two of which are to receiver admins, one has a receiver
        // of null, this is for sender to check for mails that he/she sent

        //the integrity of mail will be checked in later test

        //the code below is to test when invalid entries are entered
        originalSize = originalSize+3;
        mailController.send(-1,"invalid sender",receiverInfo,"test mail","test",time,equipmentIds);
        assertEquals(originalSize,mailService.getAll().size());

        List<String> invalidEquipmentId = new ArrayList<>();
        mailController.send(admin1.getHospitalId().getHospitalId(),admin1.getUsername(),receiverInfo,"test mail","test",time,invalidEquipmentId);
        assertEquals(originalSize,mailService.getAll().size());
        invalidEquipmentId.add("-1");
        mailController.send(admin1.getHospitalId().getHospitalId(),admin1.getUsername(),receiverInfo,"test mail","test",time,invalidEquipmentId);
        assertEquals(originalSize,mailService.getAll().size());

        List<String> invalidReceiver = new ArrayList<>();
        mailController.send(admin1.getHospitalId().getHospitalId(),admin1.getUsername(),invalidReceiver,"test mail","test",time,equipmentIds);
        assertEquals(originalSize,mailService.getAll().size());
        invalidReceiver.add("-1");
        invalidReceiver.add("invalid user");
        mailController.send(admin1.getHospitalId().getHospitalId(),admin1.getUsername(),invalidReceiver,"test mail","test",time,equipmentIds);
        assertEquals(originalSize,mailService.getAll().size());
    }

    @Test
    @Order(2)
    public void testGetReceivedMailsAndGetSentMails(){
        List<List<Object>> result = mailController.getAllReceivedMails(admin2.getHospitalId().getHospitalId(),admin2.getUsername());
        checkIntegrity(result);

        result = mailController.getAllSentMails(admin1.getHospitalId().getHospitalId(),admin1.getUsername());
        checkIntegrity(result);

        result = mailController.getAllReceivedMails(admin3.getHospitalId().getHospitalId(),admin3.getUsername());
        checkIntegrity(result);

        //the code below is to test when invalid entries are entered
        result = mailController.getAllReceivedMails(-1,"invalid user");
        assertEquals(0,result.size());
        result = mailController.getAllSentMails(-1,"invalid user");
        assertEquals(0,result.size());
    }

    private void checkIntegrity(List<List<Object>> result){
        testMail1 = (Mail) result.get(0).get(0);
        assertEquals(1,result.size());
        assertEquals("test mail",testMail1.getTitle());
        assertEquals("test",testMail1.getDescription());
        assertEquals(admin1.getUsername(),testMail1.getSenderUsername());
        assertEquals(admin1.getHospitalId().getHospitalId(),testMail1.getSenderHospitalId());
        assertEquals(testEquipment1InH1.getEquipmentId(),testMail1.getEquipments().get(0).getEquipmentId());
        assertEquals(testEquipment2InH1.getEquipmentId(),testMail1.getEquipments().get(1).getEquipmentId());
        //checking the integrity of the mail
    }

    @Test
    @Order(3)
    public void testGetSentEquipment(){
        testEquipment1InH1.setName("updated equipment 1");
        testEquipment1InH2.setName("updated equipment 2");
        //we first modify the equipment and then check whether the sent equipment has also been modified or not

        List<SentEquipment> ls = testMail1.getEquipments();
        SentEquipment equipment1 = ls.get(0);
        SentEquipment equipment2 = ls.get(1);
        assertEquals(testEquipment1InH1.getEquipmentId(),sentEquipmentController.get(equipment1.getId()).getEquipmentId());
        assertEquals("test equipment 1",sentEquipmentController.get(equipment1.getId()).getName());
        assertEquals(testEquipment2InH1.getEquipmentId(),sentEquipmentController.get(equipment2.getId()).getEquipmentId());
        assertEquals("test equipment 2",sentEquipmentController.get(equipment2.getId()).getName());

        assertNull(sentEquipmentController.get("invalid id"));
    }

    @Test
    @Order(4)
    public void testSavingSentEquipment(){
        List<SentEquipment> ls = testMail1.getEquipments();
        SentEquipment equipment1 = ls.get(0);
        SentEquipment equipment2 = ls.get(1);
        String[] ids = new String[]{equipment1.getId(),equipment2.getId()};

        assertEquals(1,testHospital3.getEquipments().size());
        sentEquipmentController.saving(admin3.getHospitalId().getHospitalId(),ids );
        testHospital3 = hospitalService.findByID(testHospital3.getHospitalId());
        //the sent equipment is now saved to hospital's database
        assertEquals(3,testHospital3.getEquipments().size());

        //the code below is to test when invalid entries are entered
        sentEquipmentController.saving(-1,ids);
        //no exceptions shall be thrown
        sentEquipmentController.saving(testHospital3.getHospitalId(),new String[]{});
        sentEquipmentController.saving(testHospital3.getHospitalId(),ids);
        assertEquals(3,testHospital3.getEquipments().size());
    }

    @Test
    @Order(5)
    public void testDeletingMails(){
        int originalSize = mailService.getAll().size();
        mailController.delete(testMail1.getId(),admin3.getHospitalId().getHospitalId(),admin3.getUsername());
        assertEquals(originalSize-1,mailService.getAll().size());

        //the code below is to test when invalid entries are entered
        mailController.delete(testMail1.getId(),-1,"invalid user");
        mailController.delete("invalid id",admin3.getHospitalId().getHospitalId(),admin3.getUsername());
        assertEquals(originalSize-1,mailService.getAll().size());
    }

    @AfterAll
    public void cleanUp(){
        trustService.delete(testTrust.getTrustId());
    }
}
