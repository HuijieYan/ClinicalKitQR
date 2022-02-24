package team7.demo.login.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "trust",orphanRemoval = true,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Hospital> hospitals = new ArrayList<>();

    public Trust(){}

    public Trust(String trustName){
        this.trustName = trustName;
        this.addHospital(new Hospital("Trust Admin",this));
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
