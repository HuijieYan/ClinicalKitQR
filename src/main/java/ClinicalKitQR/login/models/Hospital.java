package ClinicalKitQR.login.models;

import ClinicalKitQR.equipment.models.Equipment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ClinicalKitQR.equipment.models.SentEquipment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Represents a hospital that has many users
 *
 * @value hospitalId a long type integer that uniquely identifies the hospital
 * @value hospitalName name of the hospital
 * @value trust the trust which this hospital belongs to
 * @value groups stores user groups that belongs to this hospital, this is a one to many relationship
 * @value equipments stores equipments that belong to this hospital, this is a one to many relationship
 * @value sentEquipments stores copies of equipments that belong to this hospital, this is a one to many relationship
 */

@Entity(name = "Hospital")
@Table(name = "hospital")
public class Hospital {
    @SequenceGenerator(
            name = "HospitalidSeqGen",
            sequenceName = "HospitalidSequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator = "HospitalidSeqGen")
    @Column(columnDefinition = "bigint not null")
    @Id
    private long hospitalId;

    @Column(columnDefinition = "TEXT")
    private String hospitalName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trust_id",referencedColumnName = "trustId",columnDefinition = "bigint not null")
    private Trust trust;
    //the trust which this hospital belongs

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    //added OnDelete since it is part of primary key of user group
    @OneToMany(mappedBy = "hospitalId",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    //mappedBy is the name of variable in UserGroup
    private List<UserGroup> groups = new ArrayList<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "hospitalId",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Equipment> equipments = new ArrayList<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "hospitalId",fetch = FetchType.LAZY,orphanRemoval = true)
    private List<SentEquipment> sentEquipments = new ArrayList<>();

    public Hospital(){}

    public Hospital(String name,Trust trust){
        this.hospitalName = name;
        this.trust = trust;
        trust.addHospital(this);
    }

    public Hospital(String name,Trust trust,List<UserGroup>  groups){
        this.hospitalName = name;
        this.trust = trust;
        this.groups = groups;
        trust.addHospital(this);
    }

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public List<SentEquipment> getSentEquipments() {
        return sentEquipments;
    }

    public Trust getTrust() {
        return trust;
    }

    public void setTrust(Trust trust) {
        this.trust = trust;
    }

    public void addGroup(UserGroup group) {
        this.groups.add(group);
    }

    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
        equipment.setHospital(this);
    }

    public void addSentEquipment(SentEquipment equipment) {
        this.sentEquipments.add(equipment);
        equipment.setHospital(this);
    }

    public List<UserGroup> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "hospitalId=" + hospitalId +
                ", hospitalName='" + hospitalName + '\'' +
                '}';
    }
}
