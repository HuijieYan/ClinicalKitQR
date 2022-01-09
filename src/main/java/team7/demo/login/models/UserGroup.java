package team7.demo.login.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import team7.demo.issue.models.Issue;

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

    public UserGroup(){}
    //added because we must have a default constructor

    public UserGroup(String name,String username,String password,boolean isAdmin){
    //For this constructor, you MUST call addgroup function of Hospital object which this group belongs to before
    //saving this group to the database
        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public UserGroup(String name,String username,String password,Hospital hospital,boolean isAdmin){
        this.name = name;
        this.username = username;
        this.password = password;
        this.hospitalId = hospital;
        this.isAdmin = isAdmin;
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

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void addIssue(Issue issue){
        issueList.add(issue);
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