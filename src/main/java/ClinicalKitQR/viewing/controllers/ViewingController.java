package ClinicalKitQR.viewing.controllers;

import ClinicalKitQR.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.UserGroupService;
import ClinicalKitQR.viewing.models.EquipmentViewing;
import ClinicalKitQR.viewing.services.ViewingService;
import ClinicalKitQR.viewing.models.Viewing;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
@RestController
@RequestMapping(Constant.API_PREFIX+"/reports")
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

    @GetMapping("/equipmentId={equipmentId}/startDate={startDate}/endDate={endDate}")
    public  List<EquipmentViewing> getByEquipmentAndDateBetween(@PathVariable Long equipmentId, @PathVariable String startDate, @PathVariable String endDate){
        if (startDate.isEmpty() && endDate.isEmpty()){
            return viewingService.getAllByEquipmentId(equipmentId);
        } else if (!startDate.isEmpty() && !endDate.isEmpty()){
            return viewingService.getAllByEquipmentIdAndDateBetween(equipmentId,LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE_TIME), LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE_TIME));
        } else if (startDate.isEmpty() && !endDate.isEmpty()){
            return viewingService.getAllByEquipmentIdAndDateBefore(equipmentId, LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE_TIME));
        } else if (!startDate.isEmpty() && endDate.isEmpty()){
            return viewingService.getAllByEquipmentIdAndDateAfter(equipmentId, LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE_TIME));
        }
        return null;
    }

    @GetMapping("/hospitalId={hospitalId}/username={username}")
    public List<Viewing> getByUserGroup(@PathVariable long hospitalId, @PathVariable String username) {
        UserGroup userGroup = userGroupService.findByPK(hospitalId, username);
        return viewingService.getAllByUserGroup(hospitalId, username);

    }
}
