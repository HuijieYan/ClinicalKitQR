package ClinicalKitQR.equipment.models;

import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.mail.models.Mail;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * This class for storing copies of the shared equipment, so when the
 * sender changes the equipment after shared, the shared equipment will
 * not be changed
 *
 * @value saved indicates whether the receiver has saved this equipment or not
 * @value mail the sharing which shares this equipment, a sharing can share multiple equipments
 */

@Entity
@Table
public class SentEquipment{

    @Id
    private String id;

    @Column(name = "equipmentId",columnDefinition = "bigint not null")
    private long equipmentId;

    @ManyToOne(optional = false,fetch = FetchType.EAGER)
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

    @Column(columnDefinition = "TEXT")
    private String category;

    private String manufacturer;

    private String modelName;

    private LocalDate date;

    private boolean saved = false;

    @JsonIgnore
    @ManyToOne(optional = false,fetch = FetchType.EAGER)
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
        equipment.getHospitalId().addSentEquipment(this);
        this.type = equipment.getType();
        this.category = equipment.getCategory();
        this.date = equipment.getDate();
        this.searchName = name.toLowerCase();
        EquipmentModel model = equipment.getModel();
        this.manufacturer = model.getManufacturer().getManufacturerName();
        this.modelName = model.getModelName();
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

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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

    public String getModelName() {
        return modelName;
    }

    public String getManufacturer() {
        return manufacturer;
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
