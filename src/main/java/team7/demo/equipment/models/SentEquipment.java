package team7.demo.equipment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import team7.demo.issue.models.Issue;
import team7.demo.login.models.Hospital;
import team7.demo.mail.models.Mail;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class SentEquipment{

    @Id
    private String id;

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

    private boolean saved = false;

    @JsonIgnore
    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "mail_id",referencedColumnName = "id")
    private Mail mail;


    public SentEquipment(){
    }

    public SentEquipment(Equipment equipment){
        this.id = UUID.randomUUID().toString();
        this.equipmentId = equipment.getEquipmentId();
        this.name = equipment.getName();
        this.content = equipment.getContent();
        this.hospitalId = equipment.getHospitalId();
        this.type = equipment.getType();
        this.category = equipment.getCategory();
        this.date = equipment.getDate();
        this.searchName = name.toLowerCase();
    }

    public String getId() {
        return id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public boolean getSaved() {
        return saved;
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

    public Mail getMail() {
        return mail;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
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
}
