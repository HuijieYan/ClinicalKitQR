package team7.demo.login.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import team7.demo.issue.models.Issue;
import team7.demo.mail.models.Mail;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "UserGroup")
@Table(name = "usergroup")
@IdClass(UserGroupPrimaryKey.class)
public class UserGroup{
    @Column(name = "name",nullable = false,columnDefinition = "TEXT")
    private String name;

    @Id
    @Column(name = "username",nullable = false,columnDefinition = "TEXT")
    private String username;

    @Column(name = "password",nullable = false,columnDefinition = "TEXT")
    private String password;

    @Column(name = "email",nullable = true,columnDefinition = "TEXT")
    private String email;

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
    @OneToMany(mappedBy = "userGroupName",orphanRemoval = true,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Issue> issueList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiver",orphanRemoval = true,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Mail> inbox = new ArrayList<>();

    public UserGroup(){}
    //added because we must have a default constructor

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

    public void addIssue(Issue issue){
        issueList.add(issue);
    }

    public void addMail(Mail mail){
        inbox.add(mail);
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}