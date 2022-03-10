package ClinicalKitQR.mail.models;

import ClinicalKitQR.equipment.models.SentEquipment;
import ClinicalKitQR.login.models.UserGroup;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents when an admin shares one or more equipments to the other admins
 *
 * @value id an uuid that uniquely identifies the sharing
 * @value senderHospitalId stores the id of the hospital of the sender
 * @value senderUsername the username of the sender
 * @value receiver the receiver user group, can be null since when a sharing is
 *        sent the system also automatically generates a copy for the sender to
 *        view the sharings he/she sent
 * @value time the exact time(year/month/day/hour:minute:seconds) when the sharing
 *        is sent
 * @value title like title in an email
 * @value description like content in an email
 * @value equipments stores shared equipments, their data will not be changed after sending
 */

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

    @OneToMany(mappedBy = "mail",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<SentEquipment> equipments = new ArrayList<>();
    //the equipments that are shared via this sharing

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


    public void addEquipment(SentEquipment equipment){
        equipments.add(equipment);
        equipment.setMail(this);
    }


}
