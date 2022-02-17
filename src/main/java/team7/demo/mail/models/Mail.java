package team7.demo.mail.models;

import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.models.SentEquipment;
import team7.demo.login.models.UserGroup;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Mail")
@Table(name = "Mail")
public class Mail {
    @Id
    private String id;

    @Column(name = "sender_hospital_id",columnDefinition = "bigint not null")
    private long senderHospitalId;


    @Column(name = "sender_username",columnDefinition = "TEXT")
    private String senderUsername;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumns({@JoinColumn(name = "username", referencedColumnName = "username", nullable = true,columnDefinition = "TEXT"),
            @JoinColumn(name = "hospital_id",referencedColumnName = "hospital_id",columnDefinition = "bigint", nullable = true)})
    private UserGroup receiver;


    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "title",columnDefinition = "TEXT")
    private String title;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "done")
    private boolean done = false;

    @OneToMany(mappedBy = "mail",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<SentEquipment> equipments = new ArrayList<>();

    public Mail(){}

    public Mail(long senderHospitalId,String senderUsername,LocalDateTime time,String title,String description,UserGroup receiver){
        this.senderHospitalId =senderHospitalId;
        this.senderUsername = senderUsername;
        this.time = time;
        this.title = title;
        this.description = description;
        this.receiver = receiver;
        this.id = UUID.randomUUID().toString();
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public long getSenderHospitalId() {
        return senderHospitalId;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public UserGroup getReceiver() {
        return receiver;
    }

    public boolean getDone() {
        return done;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setSenderHospitalId(long senderHospitalId) {
        this.senderHospitalId = senderHospitalId;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SentEquipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<SentEquipment> equipments) {
        this.equipments = equipments;
    }

    public void setReceiver(UserGroup receiver) {
        this.receiver = receiver;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void addEquipment(SentEquipment equipment){
        equipments.add(equipment);
        equipment.setMail(this);
    }


}
