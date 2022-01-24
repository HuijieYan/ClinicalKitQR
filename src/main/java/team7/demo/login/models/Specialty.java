package team7.demo.login.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Specialty")
@Table(name = "specialty")
public class Specialty {
    @Id
    @Column(columnDefinition = "TEXT")
    private String specialty;

    @JsonIgnore
    @OneToMany(mappedBy = "specialty",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserGroup> groups = new ArrayList<>();

    public Specialty(){}

    public Specialty(String specialty){
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public List<UserGroup> getGroups() {
        return groups;
    }

    public void addGroup(UserGroup group){
        groups.add(group);
    }
}
