package team7.demo.login.models;

import java.io.Serializable;
import java.util.Objects;

public class UserGroupPrimaryKey implements Serializable {
    private String username;
    private long hospital_id;

    public UserGroupPrimaryKey(){}

    public UserGroupPrimaryKey(String username,long hospital_id){
        this.username = username;
        this.hospital_id = hospital_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGroupPrimaryKey)) return false;
        UserGroupPrimaryKey that = (UserGroupPrimaryKey) o;
        return hospital_id == that.hospital_id && username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, hospital_id);
    }

    public long getHospital_id() {
        return hospital_id;
    }

    public String getUsername() {
        return username;
    }

    public void setHospital_id(long hospital_id) {
        this.hospital_id = hospital_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
