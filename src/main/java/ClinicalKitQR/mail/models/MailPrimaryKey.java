package ClinicalKitQR.mail.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class MailPrimaryKey implements Serializable {
    private LocalDateTime time;
    private long senderHospitalId;
    private String senderUsername;

    public MailPrimaryKey(){}

    public MailPrimaryKey(LocalDateTime time,long senderHospitalId,String senderUsername){
        this.time = time;
        this.senderHospitalId = senderHospitalId;
        this.senderUsername = senderUsername;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public long getSenderHospitalId() {
        return senderHospitalId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setSenderHospitalId(long senderHospitalId) {
        this.senderHospitalId = senderHospitalId;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailPrimaryKey)) return false;
        MailPrimaryKey that = (MailPrimaryKey) o;
        return getSenderHospitalId() == that.getSenderHospitalId() && Objects.equals(getTime(), that.getTime()) && Objects.equals(getSenderUsername(), that.getSenderUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTime(), getSenderHospitalId(), getSenderUsername());
    }
}
