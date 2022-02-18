package team7.demo.viewing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.UserGroupService;
import team7.demo.viewing.services.ViewingService;
import team7.demo.viewing.models.Viewing;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ViewingController {
    private final ViewingService viewingService;
    private final EquipmentService equipmentService;
    private final UserGroupService userGroupService;

    @Autowired
    public ViewingController(ViewingService viewingService, EquipmentService equipmentService, UserGroupService userGroupService) {
        this.viewingService = viewingService;
        this.equipmentService = equipmentService;
        this.userGroupService = userGroupService;
    }

    @GetMapping("/equipmentId={equipmentId}")
    public List<Viewing> getByEquipment(@PathVariable long equipmentId) {
        Equipment equipment = equipmentService.get(equipmentId);
        return viewingService.getAllByEquipment(equipmentId);
    }

    @GetMapping("/hospitalId={hospitalId}/username={username}")
    public List<Viewing> getByUserGroup(@PathVariable long hospitalId, @PathVariable String username) {
        UserGroup userGroup = userGroupService.findByPK(hospitalId, username);
        return viewingService.getAllByUserGroup(userGroup);

    }
}
