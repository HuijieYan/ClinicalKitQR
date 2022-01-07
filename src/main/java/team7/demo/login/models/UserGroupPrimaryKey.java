package team7.demo.login.models;

import java.io.Serializable;
import java.util.Objects;

public class UserGroupPrimaryKey implements Serializable {
    private String username;
    private long hospitalId;

    public UserGroupPrimaryKey(){}

    public UserGroupPrimaryKey(String username,long hospitalId){
        this.username = username;
        this.hospitalId = hospitalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGroupPrimaryKey)) return false;
        UserGroupPrimaryKey that = (UserGroupPrimaryKey) o;
        return hospitalId == that.hospitalId && username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, hospitalId);
    }

    public long getHospitalId() {
        return hospitalId;
    }

    public String getUsername() {
        return username;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
