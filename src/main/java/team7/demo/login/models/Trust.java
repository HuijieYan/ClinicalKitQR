package team7.demo.login.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Trust")
public class Trust {
    @SequenceGenerator(
            name = "TrustidSeqGen",
            sequenceName = "TrustidSequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator = "TrustidSeqGen")
    @Column(columnDefinition = "bigint not null")
    @Id
    private long trustId;

    @Column(columnDefinition = "TEXT")
    private String trustName;

    @OneToMany(mappedBy = "trust",orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Hospital> hospitals = new ArrayList<>();

    public Trust(){}

    public Trust(String trustName){
        this.trustName = trustName;
    }

    public long getTrustId() {
        return trustId;
    }

    public String getTrustName() {
        return trustName;
    }

    public void setTrustName(String trustName) {
        this.trustName = trustName;
    }

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
    }

    public void addHospital(Hospital hospital){
        this.hospitals.add(hospital);
        hospital.setTrust(this);
    }

    @Override
    public String toString() {
        return "Trust{" +
                "trustId=" + trustId +
                ", trustName='" + trustName + '\'' +
                '}';
    }
}
