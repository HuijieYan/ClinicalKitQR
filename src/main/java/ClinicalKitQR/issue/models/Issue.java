package ClinicalKitQR.issue.models;

import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.login.models.UserGroup;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "Issue")
@Table(name = "Issue")
public class Issue {
    @Id
    @SequenceGenerator(
            name = "IssueidSeqGen",
            sequenceName = "IssueidSequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator = "IssueidSeqGen")
    @Column(name = "issue_id",columnDefinition = "bigint not null")
    private long issueId;

    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumns(
            {@JoinColumn(name = "username", referencedColumnName = "username", nullable = false,columnDefinition = "TEXT"),
            @JoinColumn(name = "hospital_id",referencedColumnName = "hospital_id",columnDefinition = "bigint not null")}
    )
    private UserGroup userGroupName;

    @Column(columnDefinition = "Text")
    private String description;

    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(
            name = "equipment_id",
            referencedColumnName = "equipmentId",
            columnDefinition = "bigint not null"
    )
    private Equipment equipmentId;

    @Column(name = "solved")
    private boolean solved;

    @Column(name = "Date")
    private LocalDate date;

    public Issue(){
    }

    public Issue(LocalDate date,UserGroup group,Equipment equipment,String description){
        this.date = date;
        this.userGroupName = group;
        this.equipmentId = equipment;
        this.description = description;
        this.solved = false;
        //solved is set to false by default
    }

    public Issue(LocalDate date,UserGroup group,Equipment equipment,String description,boolean solved){
        this.date = date;
        this.userGroupName = group;
        this.equipmentId = equipment;
        this.description = description;
        this.solved = solved;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public boolean isSolved() {
        return solved;
    }

    public long getIssueId() {
        return issueId;
    }

    public Equipment getEquipmentId() {
        return equipmentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public UserGroup getUserGroupName() {
        return userGroupName;
    }

    public String getDescription() {
        return description;
    }
}
