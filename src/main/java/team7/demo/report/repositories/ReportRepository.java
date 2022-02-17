package team7.demo.report.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team7.demo.report.models.Report;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r from report r where r.equipmentId = ?1")
    public List<Report> getAllByEquipmentId(Long id);

    @Query("select r from report  r where r.date = ?1")
    public List<Report> getAllByDate(LocalDate date);

}
