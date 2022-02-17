package team7.demo.report.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.demo.equipment.services.EquipmentService;
import team7.demo.report.models.Report;
import team7.demo.report.services.ReportService;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private EquipmentService equipmentService;

    public ReportController(ReportService reportService, EquipmentService equipmentService) {
        this.reportService = reportService;
        this.equipmentService = equipmentService;
    }

    @GetMapping("/equipmentId={equipmentId}")
    public List<Report> getByEquipment(@PathVariable long equipmentId) {
        return reportService.getAllByEquipment(equipmentId);
    }
}
