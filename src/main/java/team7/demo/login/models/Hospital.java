package team7.demo.login.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import team7.demo.equipment.models.Equipment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "trust_id",referencedColumnName = "trustId",columnDefinition = "bigint not null")
    private Trust trust;

    @JsonIgnore
    @OneToMany(mappedBy = "hospitalId",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    //mappedBy is the name of variable in UserGroup
    private List<UserGroup> groups = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "hospitalId",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Equipment> equipments = new ArrayList<>();

    public Hospital(){}

    public Hospital(String name,Trust trust){
        this.hospitalName = name;
        this.trust = trust;
    }

    public Hospital(String name,Trust trust,List<UserGroup>  groups){
        this.hospitalName = name;
        this.trust = trust;
        this.groups = groups;
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

    public Trust getTrust() {
        return trust;
    }

    public void setTrust(Trust trust) {
        this.trust = trust;
    }

    public void addGroup(UserGroup group) {
        this.groups.add(group);
        group.setHospital(this);
    }

    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
        equipment.setHospital(this);
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "hospitalId=" + hospitalId +
                ", hospitalName='" + hospitalName + '\'' +
                '}';
    }
}
