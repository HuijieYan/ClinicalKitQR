package team7.demo.report.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.report.models.Report;
import team7.demo.report.repositories.ReportRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository repository;

    @Autowired
    public ReportService(ReportRepository repository) {
        this.repository = repository;
    }

    public List<Report> getAllByEquipment(Long id) {
        return repository.getAllByEquipmentId(id);
    }

    public  List<Report> getAllByDate(LocalDate date) {
        return repository.getAllByDate(date);
    }

    public void save(Report report) {
        repository.save(report);
    }

}
