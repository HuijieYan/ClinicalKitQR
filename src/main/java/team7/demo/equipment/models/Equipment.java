package team7.demo.equipment.models;

import javax.imageio.ImageIO;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import team7.demo.Constant;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import team7.demo.issue.models.Issue;
import team7.demo.login.models.Hospital;
import team7.demo.login.models.UserGroup;
import team7.demo.mail.models.Mail;
import team7.demo.viewing.models.Viewing;

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

    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "hospitalId",
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
    //the type of the equipment ie. equipment for stomach

    @Column(columnDefinition = "TEXT")
    private String category;
    //the category of the equipment, can be Neonatal or Adult or Child

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

    public Equipment(String name,String content,Hospital hospitalId,String type,String category){
        this.name = name;
        this.content = content;
        this.hospitalId = hospitalId;
        this.type = type;
        this.category = category;
        this.date = LocalDate.now();
        this.searchName = name.toLowerCase();
    }

    public Equipment(Equipment equipment){
        this.name = equipment.getName();
        this.content = equipment.getContent();
        this.hospitalId = equipment.getHospitalId();
        this.type = equipment.getType();
        this.category = equipment.getCategory();
        this.date = equipment.getDate();
        this.searchName = name.toLowerCase();
    }

    public Equipment(SentEquipment equipment,Hospital hospital){
        this.name = equipment.getName();
        this.content = equipment.getContent();
        this.hospitalId = hospital;
        this.type = equipment.getType();
        this.category = equipment.getCategory();
        this.date = equipment.getDate();
        this.searchName = name.toLowerCase();
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

    public void addViewing(Viewing viewing) {
        viewingList.add(viewing);
    }
}
