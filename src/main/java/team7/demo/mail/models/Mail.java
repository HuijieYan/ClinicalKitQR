package team7.demo.mail.models;

import team7.demo.equipment.models.Equipment;
import team7.demo.login.models.UserGroup;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Mail")
@Table(name = "Mail")
@IdClass(MailPrimaryKey.class)
public class Mail {
    @Id
    @Column(name = "sender_hospital_id",columnDefinition = "bigint not null")
    private long senderHospitalId;

    @Id
    @Column(name = "sender_username",columnDefinition = "TEXT")
    private String senderUsername;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "mail_receivers",
            joinColumns = {@JoinColumn(name = "senderHospitalId"),@JoinColumn(name = "senderUsername"),@JoinColumn(name = "time")},
            inverseJoinColumns = {@JoinColumn(name = "username", referencedColumnName = "username", nullable = false,columnDefinition = "TEXT"),
                    @JoinColumn(name = "hospital_id",referencedColumnName = "hospital_id",columnDefinition = "bigint not null")}
    )
    private List<UserGroup> receiver = new ArrayList<>();

    @Id
    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "title",columnDefinition = "TEXT")
    private String title;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "done")
    private boolean done = false;

    @ManyToMany
    @JoinTable(
            name = "equipments_in_mail",
            joinColumns = {@JoinColumn(name = "senderHospitalId"),@JoinColumn(name = "senderUsername"),@JoinColumn(name = "time")},
            inverseJoinColumns = @JoinColumn(name = "equipmentId"))
    private List<Equipment> equipments = new ArrayList<>();

    public Mail(){}

    public Mail(long senderHospitalId,String senderUsername,LocalDateTime time,String title,String description){
        this.senderHospitalId =senderHospitalId;
        this.senderUsername = senderUsername;
        this.time = time;
        this.title = title;
        this.description = description;
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

    public List<UserGroup> getReceiver() {
        return receiver;
    }

    public boolean getDone() {
        return done;
    }

    public LocalDateTime getTime() {
        return time;
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

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public void setReceiver(List<UserGroup> receiver) {
        this.receiver = receiver;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void addEquipment(Equipment equipment){
        equipments.add(equipment);
    }

    public void addReceiver(UserGroup group){
        receiver.add(group);
    }

}
