package team7.demo.issue.models;

import team7.demo.login.models.UserGroup;

import javax.persistence.*;

//@Entity(name = "Issue")
//@Table(name = "Issue")
public class Issue {



    @Column(columnDefinition = "Text")
    private String usergroupName;

    @Column(columnDefinition = "Text")
    private String description;
}
