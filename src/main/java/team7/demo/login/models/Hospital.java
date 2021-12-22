package team7.demo.login.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Hospital")
@Table(name = "hospital")
public class Hospital {
    @SequenceGenerator(
            name = "HospitalidSeqGen",
            sequenceName = "HospitalidSequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator = "HospitalidSeqGen")
    @Column(name = "hospitalId",columnDefinition = "bigint not null")
    @Id
    private long hospitalId;

    @Column(columnDefinition = "TEXT")
    private String hospitalName;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "trust_id",referencedColumnName = "trustId",columnDefinition = "bigint not null")
    private Trust trust;

    @OneToMany(mappedBy = "hospital_id",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserGroup> groups = new ArrayList<>();

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

    public long getId() {
        return hospitalId;
    }

    public String getName() {
        return hospitalName;
    }

    public void setId(long id) {
        this.hospitalId = id;
    }

    public void setName(String name) {
        this.hospitalName = name;
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

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + hospitalId +
                ", name='" + hospitalName + '\'' +
                ", trust='" + trust + '\'' +
                '}';
    }
}
