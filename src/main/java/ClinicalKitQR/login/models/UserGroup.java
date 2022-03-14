package ClinicalKitQR.login.models;


import ClinicalKitQR.issue.models.Issue;
import ClinicalKitQR.mail.models.Mail;
import ClinicalKitQR.viewing.models.Viewing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the user of this website
 *
 * @value name the display name of the user group
 * @value username the username of the user group, must be unique within the same hospital,
 *        this is a part of the composite primary key
 * @value password there's no restrictions to ensure safety
 * @value email optional, the email of this group, often used with admin users
 * @value specialty optional, this group's specialty, often used with admin users
 * @value hospitalId the hospital which this group belongs
 * @value isAdmin indicates whether this group is an admin or not
 * @value issueList stores the issues which this group reported
 * @value inbox stores sharings that this group received
 * @value viewingList stores the viewing history of this group
 */

@Entity(name = "UserGroup")
@Table(name = "usergroup")
@IdClass(UserGroupPrimaryKey.class)
public class UserGroup{
    @Column(name = "name",nullable = false,columnDefinition = "TEXT")
    private String name;

    @Id
    @Column(name = "username",nullable = false,columnDefinition = "TEXT")
    private String username;

    @JsonIgnore
    //you can't access the password of the user group if the object is passed
    @Column(name = "password",nullable = false,columnDefinition = "TEXT")
    private String password;

    @Column(name = "email",nullable = true,columnDefinition = "TEXT")
    private String email = "";

    @Column(name = "specialty",nullable = true,columnDefinition = "TEXT")
    private String specialty = "";

    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    //persist for saving a new group without touching hospital
    @JoinColumn(
            name = "hospital_id",
            referencedColumnName = "hospitalId",
            columnDefinition = "bigint not null"
    )
    //reference is the variable in Hospital
    @Id
    private Hospital hospitalId;
    //the name of this variable must be the same as the long variable in UserGroupPrimaryKey

    @Column(name="is_admin",nullable = false)
    private boolean isAdmin;

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "userGroupName",orphanRemoval = true,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Issue> issueList = new ArrayList<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "receiver",orphanRemoval = true,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Mail> inbox = new ArrayList<>();

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(
            mappedBy = "userGroup",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Viewing> viewingList = new ArrayList<>();

    public UserGroup(){}
    //added because we must have a default constructor

    public UserGroup(String name,String username,String password,Hospital hospital,boolean isAdmin,String email,String specialty){
        this.name = name;
        this.username = username;
        this.password = password;
        this.hospitalId = hospital;
        this.email = email;
        this.specialty = specialty;
        if (hospital.getHospitalName().equals("Trust Admin")){
            this.isAdmin = true;
        }else{
            this.isAdmin = isAdmin;
        }
    }

    public UserGroup(String name,String username,String password,Hospital hospital,boolean isAdmin,String email){
        this.name = name;
        this.username = username;
        this.password = password;
        this.hospitalId = hospital;
        this.email = email;
        if (hospital.getHospitalName().equals("Trust Admin")){
            this.isAdmin = true;
        }else{
            this.isAdmin = isAdmin;
        }
    }

    public UserGroup(String name,String username,String password,Hospital hospital,boolean isAdmin){
        this.name = name;
        this.username = username;
        this.password = password;
        this.hospitalId = hospital;
        if (hospital.getHospitalName().equals("Trust Admin")){
            this.isAdmin = true;
        }else{
            this.isAdmin = isAdmin;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHospitalId(Hospital hospital_id) {
        this.hospitalId = hospital_id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Hospital getHospitalId() {
        return hospitalId;
    }

    public String getSpecialty() {
        return specialty;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setSpecialty(String speciality) {
        this.specialty = speciality;
    }

    public void addIssue(Issue issue){
        issueList.add(issue);
    }

    public void addMail(Mail mail){
        inbox.add(mail);
    }

    public void addViewing(Viewing viewing) {
        viewingList.add(viewing);
    }
}