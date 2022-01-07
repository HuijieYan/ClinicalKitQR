package team7.demo.login.models;


import javax.persistence.*;

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

    public UserGroup(){}
    //added because we must have a default constructor

    public UserGroup(String name,String username,String password){
    //For this constructor, you MUST call addgroup function of Hospital object which this group belongs to before
    //saving this group to the database
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public UserGroup(String name,String username,String password,Hospital hospital){
        this.name = name;
        this.username = username;
        this.password = password;
        this.hospitalId = hospital;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHospital(Hospital hospital_id) {
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

    public Hospital getHospital() {
        return hospitalId;
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