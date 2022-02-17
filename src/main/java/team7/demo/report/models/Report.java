package team7.demo.report.models;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "report")
public class Report
{
    @Id
    @SequenceGenerator(
            name = "report_sequence",
            sequenceName = "report_sequence_name",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "report_sequence"
    )
    @Column(
            name = "report_id",
            updatable = false
    )
    private Long reportId;
    @Column(
            name = "equipment_id",
            nullable = false
    )
    private Long equipmentId;
    @Column(
            name = "date",
            nullable = false,
            columnDefinition = "DATE DEFAULT CURRENT_DATE"
    )
    private LocalDate date;

    public Report(Long reportId, Long equipmentId, LocalDate date)
    {
        this.reportId = reportId;
        this.equipmentId = equipmentId;
        this.date = date;
    }

    public Report() {

    }

    public Long getReportId()
    {
        return reportId;
    }

    public void setReportId(Long reportId)
    {
        this.reportId = reportId;
    }

    public Long getEquipmentId()
    {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "Report{" +
                "reportId=" + reportId +
                ", equipmentId=" + equipmentId +
                ", date='" + date + '\'' +
                '}';
    }
}
