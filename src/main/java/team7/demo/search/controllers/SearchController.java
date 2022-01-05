package team7.demo.search.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.demo.equipment.services.EquipmentService;
import team7.demo.login.services.HospitalService;
import team7.demo.login.services.TrustService;
import team7.demo.login.services.UserGroupService;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final UserGroupService userGroupService;
    private final EquipmentService equipmentService;
    private final HospitalService hospitalService;
    private final TrustService trustService;

    @Autowired
    public SearchController(UserGroupService userGroupService,EquipmentService equipmentService,HospitalService hospitalService,TrustService trustService){
       this.userGroupService = userGroupService;
       this.equipmentService = equipmentService;
       this.hospitalService = hospitalService;
       this.trustService = trustService;
    }

    @GetMapping("/str={str}")
    public void search(@PathVariable String str){

    }
}
