package ClinicalKitQR;


import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.HospitalService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ClinicalKitQR.login.services.UserGroupService;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoApplicationTests {
	private Trust trust = new Trust("Test Trust");
	private Hospital hospital = new Hospital("Test Hospital",trust);
	private long hospital_id;

	@Autowired
	private UserGroupService service;
	@Autowired
	private HospitalService hospitalService;


	@BeforeAll
	public void setUp(){
		hospital = hospitalService.save(hospital);
		hospital_id = hospital.getHospitalId();
	}

	@Test
	@Order(2)
	void testLoadingUserGroupDatabase() {
		List<UserGroup> groups = service.getAll();
		Assertions.assertTrue(groups.size()>=0);
	}

	@Test
	@Order(1)
	void testSavingUserGroupSuccess(){
	//check whether
		int originalSize = service.getAll().size();
		UserGroup group = new UserGroup("test1","test1","test1",hospital,true);
		//hospital.addGroup(group);
		service.save(group);
		Assertions.assertTrue(originalSize+1==service.getAll().size());
	}

	@Test
	@Order(3)
	void testFindingUserGroupByUserName(){
		UserGroup groups = service.findByPK(hospital_id,"test1");
		Assertions.assertTrue(groups!=null);
	}

	@Test
	@Order(4)
	void testSavingUserGroupFailed(){
		int originalSize = service.getAll().size();
		UserGroup group = new UserGroup("test1","test1","test1",hospital,true);
		//hospital.addGroup(group);
		service.save(group);
		Assertions.assertTrue(originalSize==service.getAll().size());
	}

}