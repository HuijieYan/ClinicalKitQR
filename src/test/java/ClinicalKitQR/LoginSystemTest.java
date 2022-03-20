package ClinicalKitQR;

import ClinicalKitQR.login.controllers.HospitalController;
import ClinicalKitQR.login.controllers.TrustController;
import ClinicalKitQR.login.controllers.UserGroupController;
import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.HospitalService;
import ClinicalKitQR.login.services.TrustService;
import ClinicalKitQR.login.services.UserGroupService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  For login system, we are testing TrustController, HospitalController and UserGroupController
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginSystemTest {
    private Trust trust;
    private Hospital trustAdminHos;
    private UserGroup trustAdmin;
    private Hospital testHospital;
    private UserGroup testHospitalAdmin;
    private UserGroup testUser;

    @Autowired
    private UserGroupController userGroupController;
    @Autowired
    private TrustController trustController;
    @Autowired
    private HospitalController hospitalController;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private TrustService trustService;

    @Test
    @Order(1)
    public void testAddTrust() {
        int originalTrustSize = trustService.getAll().size();
        trustController.addTrust("Test Trust", "admin", "123", "trust admin", "trustAdmin@email.com", "admin");
        assertTrue(trustService.getAll().size() == originalTrustSize + 1);
        trust = trustService.getAll().get(originalTrustSize);
        //since the new trust will have the latest ID + 1 as its id
        assertTrue(trust.getTrustName().equals("Test Trust"));
        assertTrue(trust.getHospitals().size() == 1);
        //only trust admin hospital is created
        trustAdminHos = trust.getHospitals().get(0);
        assertTrue(trustAdminHos.getHospitalName().equals("Trust Admin"));
        assertTrue(trustAdminHos.getGroups().size() == 1);
        trustAdmin = trustAdminHos.getGroups().get(0);
        assertTrue(trustAdmin.getIsAdmin()&&trustAdmin.getSpecialty().equals("admin")&&
                trustAdmin.getEmail().equals("trustAdmin@email.com")&&trustAdmin.getUsername().equals("admin")&&
                trustAdmin.getPassword().equals("123")&&trustAdmin.getName().equals("trust admin")&&
                trustAdmin.getHospitalId().getHospitalId() == trustAdminHos.getHospitalId());

        //the code below is to test when invalid entries are entered
        trustController.addTrust("", "admin", "123", "trust admin", "trustAdmin@email.com", "admin");
        assertEquals(originalTrustSize+1,trustService.getAll().size());
        trustController.addTrust("Test Trust", "", "123", "trust admin", "trustAdmin@email.com", "admin");
        assertEquals(originalTrustSize+1,trustService.getAll().size());
        trustController.addTrust("Test Trust", "admin", "", "trust admin", "trustAdmin@email.com", "admin");
        assertEquals(originalTrustSize+1,trustService.getAll().size());
        trustController.addTrust("Test Trust", "admin", "123", "", "trustAdmin@email.com", "admin");
        assertEquals(originalTrustSize+1,trustService.getAll().size());
    }

    @Test
    @Order(2)
    public void testGetAllTrust(){
        int originalTrustSize = trustService.getAll().size();
        assertEquals(originalTrustSize,trustController.getAll().size());
    }

    @Test
    @Order(3)
    public void testGetTrustById(){
        assertNotNull(trustController.getById(trust.getTrustId()));
    }

    @Test
    @Order(4)
    public void testRegisterNewHospital(){
        int originalHospitalSize = hospitalService.getAll().size();
        hospitalController.register(trust.getTrustId(),"Test Hospital");
        assertEquals(originalHospitalSize+1,hospitalService.getAll().size());
        testHospital = hospitalService.findAll().get(originalHospitalSize);
        //since the new hospital will have the latest ID + 1 as its id
        assertEquals(testHospital.getHospitalName(),"Test Hospital");

        //the code below is to test when invalid entries are entered
        hospitalController.register(trust.getTrustId(),"");
        assertEquals(originalHospitalSize+1,hospitalService.getAll().size());
        hospitalController.register(trust.getTrustId(),null);
        assertEquals(originalHospitalSize+1,hospitalService.getAll().size());
    }

    @Test
    @Order(5)
    public void testGetHospital(){
        Hospital result = hospitalController.getHospital(testHospital.getHospitalId());
        assertEquals(testHospital.getHospitalId(),result.getHospitalId());
        assertEquals(testHospital.getHospitalName(),result.getHospitalName());
    }

    @Test
    @Order(6)
    public void testGetAllHospitalByTrustAndGetAllHospitals(){
        int originalHospitalSize = hospitalService.getAll().size();
        hospitalController.register(trust.getTrustId(),"Test Hospital 2");
        assertEquals(hospitalController.getAllByTrust(trust.getTrustId()).size(),3);
        //trust admin, test hospital and test hospital 2
        assertEquals(hospitalController.getAll().size(),originalHospitalSize+1);
    }

    @Test
    @Order(7)
    public void testUpdateHospital(){
        hospitalController.update(testHospital.getHospitalId(),"Updated Test Hospital");
        testHospital = hospitalController.getHospital(testHospital.getHospitalId());
        assertEquals(testHospital.getHospitalName(),"Updated Test Hospital");

        //code below is to test whether this function accepts invalid entries or not
        hospitalController.update(testHospital.getHospitalId(),"");
        testHospital = hospitalController.getHospital(testHospital.getHospitalId());
        assertEquals(testHospital.getHospitalName(),"Updated Test Hospital");

        hospitalController.update(testHospital.getHospitalId(),null);
        testHospital = hospitalController.getHospital(testHospital.getHospitalId());
        assertEquals(testHospital.getHospitalName(),"Updated Test Hospital");
    }

    @Test
    @Order(8)
    public void testRegisterUsergroup(){
        userGroupController.register(testHospital.getHospitalId(),"test admin 1","g1","123",true,"","");
        userGroupController.register(testHospital.getHospitalId(),"test group 1","g2","123",false,"","");
        assertEquals(userGroupService.getAllByHospital(testHospital.getHospitalId()).size(),2);

        //the code below is to test when invalid entries are entered
        userGroupController.register(testHospital.getHospitalId(),"","g3","123",true,"","");
        assertEquals(userGroupService.getAllByHospital(testHospital.getHospitalId()).size(),2);
        userGroupController.register(testHospital.getHospitalId(),"test group 1","","123",true,"","");
        assertEquals(userGroupService.getAllByHospital(testHospital.getHospitalId()).size(),2);
        userGroupController.register(testHospital.getHospitalId(),"test group 1","g3","",true,"","");
        assertEquals(userGroupService.getAllByHospital(testHospital.getHospitalId()).size(),2);

        userGroupController.register(testHospital.getHospitalId(),"test admin 3","g1","123",true,"","");
        assertEquals(userGroupService.getAllByHospital(testHospital.getHospitalId()).size(),2);
        //when a new group with same username entered, this is rejected

        testHospitalAdmin = userGroupService.findByPK(testHospital.getHospitalId(),"g1");
        testUser = userGroupService.findByPK(testHospital.getHospitalId(),"g2");
    }

    @Test
    @Order(9)
    public void testLogin(){
        List<String> result = userGroupController.login(testHospital.getHospitalId(),"g1","123");
        assertEquals(result.get(0),"2");
        assertEquals(result.get(1),Long.toString(testHospital.getHospitalId()));
        assertEquals(result.get(2),Long.toString(trust.getTrustId()));
        assertEquals(result.get(3),testHospitalAdmin.getName());
        //proving that the content returned must be correct

        result = userGroupController.login(testHospital.getHospitalId(),"g2","123");
        assertEquals(result.get(0),"1");
        assertEquals(result.get(1),Long.toString(testHospital.getHospitalId()));
        assertEquals(result.get(2),Long.toString(trust.getTrustId()));
        assertEquals(result.get(3),testUser.getName());

        result = userGroupController.login(trustAdminHos.getHospitalId(),"admin","123");
        assertEquals(result.get(0),"3");
        assertEquals(result.get(1),Long.toString(trustAdminHos.getHospitalId()));
        assertEquals(result.get(2),Long.toString(trust.getTrustId()));
        assertEquals(result.get(3),trustAdmin.getName());

        assertTrue(userGroupController.login(testHospital.getHospitalId(),"g2","321").isEmpty());
        assertTrue(userGroupController.login(testHospital.getHospitalId(),"g3","123").isEmpty());
        //when incorrect detail is entered, empty array is returned
    }

    @Test
    @Order(10)
    public void testGetUsergroupById(){
        UserGroup result = userGroupController.getById(testHospital.getHospitalId(),"g2");
        assertEquals(result.getUsername(),testUser.getUsername());
        assertEquals(result.getPassword(),testUser.getPassword());
        assertEquals(result.getHospitalId().getHospitalId(),testUser.getHospitalId().getHospitalId());

        assertNull(userGroupController.getById(testHospital.getHospitalId(),"g3"));
        assertNull(userGroupController.getById(testHospital.getHospitalId(),""));
        //when incorrect detail is entered, null is returned
    }

    @Test
    @Order(11)
    public void testGetAllAdmin(){
        assertTrue(userGroupController.getAllAdmin().size()>=2);
        //since we've created one trust admin and one hospital admin already, the minimum number of admins is 2
        //because this test can be run at any time, we can't assess the detail of the admin
    }

    @Test
    @Order(12)
    public void testGetAllUsergroupByHospital(){
        List<List<String>> result = userGroupController.getAllByHospital(testHospital.getHospitalId());

        assertEquals(result.size(), 2);
        List<String> hospitalAdminDetail = result.get(0);
        assertEquals(hospitalAdminDetail.get(0),testHospitalAdmin.getName());
        assertEquals(hospitalAdminDetail.get(1),testHospitalAdmin.getUsername());
        assertEquals(hospitalAdminDetail.get(2),"Hospital Admin");
        assertEquals(hospitalAdminDetail.get(3),testHospital.getHospitalName());
        assertEquals(hospitalAdminDetail.get(4),Long.toString(testHospital.getHospitalId()));
        //validating the details of the returned list, proving that it should be correct

        assertTrue(userGroupController.getAllByHospital(-1).isEmpty());
        //if incorrect detail is entered, empty array is returned
    }

    @Test
    @Order(13)
    public void testUpdateUsergroup(){
        userGroupController.update(testHospital.getHospitalId(),testUser.getUsername(),"new name","321","","",true);
        testUser = userGroupController.getById(testHospital.getHospitalId(),"g2");
        assertEquals(testUser.getName(),"new name");
        assertEquals(testUser.getPassword(),"321");
        assertTrue(testUser.getIsAdmin());
        //the details shall be changed by now

        userGroupController.update(testHospital.getHospitalId(),testUser.getUsername(),"","","","",true);
        assertEquals(testUser.getName(),"new name");
        assertEquals(testUser.getPassword(),"321");
        //controller does not accept name and password to be empty

        assertEquals(userGroupController.update(testHospital.getHospitalId(),"g3","new name","321","","",true),"Error: User details are invalid");
        //when incorrect detail is entered, return error message
    }

    @Test
    @Order(14)
    public void testDeleteUsergroup(){
        userGroupController.delete(testHospital.getHospitalId(),testUser.getUsername());
        assertEquals(userGroupController.getAllByHospital(testHospital.getHospitalId()).size(),1);
        //after deleted the user, there shall only be hospital admin left

        userGroupController.delete(testHospital.getHospitalId(),testUser.getUsername());
        assertEquals(userGroupController.getAllByHospital(testHospital.getHospitalId()).size(),1);
        //when a non-existing user is deleted, nothing happens
    }

    @Test
    @Order(15)
    public void testDeleteHospital(){
        int originalHospitalSize = hospitalController.getAll().size();
        trust = trustController.getById(trust.getTrustId());
        int originalHospitalInTrust = trust.getHospitals().size();
        assertTrue(hospitalController.delete(testHospital.getHospitalId()));
        trust = trustController.getById(trust.getTrustId());
        assertEquals(trust.getHospitals().size(),originalHospitalInTrust-1);
        assertNull(userGroupController.getById(testHospital.getHospitalId(),testHospitalAdmin.getUsername()));
        assertEquals(hospitalController.getAll().size(),originalHospitalSize-1);
        //when delete is successful there should be one hospital (trust admin) left

        assertFalse(hospitalController.delete(testHospital.getHospitalId()));
        //when a non-existing hospital is deleted, false is returned
    }

    @Test
    @Order(16)
    public void testDeleteTrust(){
        int originalTrustSize = trustController.getAll().size();
        assertTrue(trustController.delete(trust.getTrustId()));
        assertEquals(trustController.getAll().size(),originalTrustSize-1);
        assertNull(hospitalController.getHospital(trustAdminHos.getHospitalId()));
        assertNull(userGroupController.getById(trustAdminHos.getHospitalId(),trustAdmin.getUsername()));
        //when trust is deleted, all data within the trust are deleted

        assertFalse(trustController.delete(trust.getTrustId()));
        //when a non-existing trust is deleted, false is returned
    }
}
