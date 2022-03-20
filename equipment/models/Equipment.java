package ClinicalKitQR.equipment.models;

import javax.persistence.*;

import ClinicalKitQR.viewing.models.Viewing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ClinicalKitQR.issue.models.Issue;
import ClinicalKitQR.login.models.Hospital;

/**
 * Represents the equipment education page, all values below are not nullable
 *
 * @value equipmentId a Long type integer
 * @value hospitalId the owner (hospital) of this page
 * @value name the display name of the equipment
 * @value searchName the name that will be used when searching equipment, must be lowercase letters and no spaces
 * @value content a string containing the html code of the page
 * @value type the type of clinical system of the equipment i.e. equipment for stomach
 * @value category the patient demographic of the equipment, can be Neonatal or Adult or Child
 * @value model the model of the equipment i.e. UX-001
 * @value date the year when this equipment was created
 * @value issueList stores the issues related to this equipment, this is a one to many relationship
 * @value viewingList stores the issues related to this equipment, this is a one to many relationship
 */

@Entity(name = "Equipment")
@Table(name = "Equipment")
public class Equipment {
    @Id
    @SequenceGenerator(
            name = "EquipmentidSeqGen",
            sequenceName = "EquipmentidSequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator = "EquipmentidSeqGen")
    @Column(name = "equipmentId",columnDefinition = "bigint not null")
    private long equipmentId;

    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    @JoinColumn(
            name = "hospitalId",
            referencedColumnName = "hospitalId",
            columnDefinition = "bigint not null"
    )
    public Hospital hospitalId;

    @Column(columnDefinition = "TEXT")
    private String searchName;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String type;

    @Column(columnDefinition = "TEXT")
    private String category;

    @OneToOne(orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "modelId")
    private EquipmentModel model;

    private LocalDate date;

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "equipmentId",orphanRemoval = true,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Issue> issueList = new ArrayList<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(
            mappedBy = "equipmentId",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Viewing> viewingList = new ArrayList<>();


    public Equipment(){}

    public Equipment(String name,String content,Hospital hospitalId,String type,String category,EquipmentModel model){
        this.name = name;
        this.content = content;
        this.hospitalId = hospitalId;
        this.type = type;
        this.category = category;
        this.date = LocalDate.now();
        this.searchName = name.replaceAll(" ","").toLowerCase();
        this.setModel(model);
        hospitalId.addEquipment(this);
    }

    public Equipment(Equipment equipment){
        this.name = equipment.getName();
        this.content = equipment.getContent();
        this.hospitalId = equipment.getHospitalId();
        this.type = equipment.getType();
        this.category = equipment.getCategory();
        this.date = equipment.getDate();
        this.setModel(new EquipmentModel(equipment.getModel()));
        this.searchName = name.replaceAll(" ","").toLowerCase();
        hospitalId.addEquipment(this);
    }

    public Equipment(SentEquipment equipment,Hospital hospital,Manufacturer manufacturer){
        this.name = equipment.getName();
        this.content = equipment.getContent();
        this.hospitalId = hospital;
        this.type = equipment.getType();
        this.category = equipment.getCategory();
        this.date = equipment.getDate();
        this.setModel(new EquipmentModel(equipment.getModelName(),manufacturer));
        this.searchName = name.replaceAll(" ","").toLowerCase();
        hospitalId.addEquipment(this);
    }


    public void setModel(EquipmentModel model) {
        model.setEquipment(this);
        this.model = model;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public void setHospital(Hospital hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public Hospital getHospitalId() {
        return hospitalId;
    }

    public void addIssue(Issue issue){
        issueList.add(issue);
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getSearchName() {
        return searchName;
    }

    public EquipmentModel getModel() {
        return model;
    }

    public void addViewing(Viewing viewing) {
        viewingList.add(viewing);
    }
}