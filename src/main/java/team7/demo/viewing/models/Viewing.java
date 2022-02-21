package team7.demo.viewing.models;

import team7.demo.equipment.models.Equipment;
import team7.demo.login.models.UserGroup;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Viewing")
public class Viewing
{
    @Id
    private String viewingId;

    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "equipment_id",
            nullable = false,
            referencedColumnName = "equipmentId"
    )
    private Equipment equipmentId;

    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumns(
            {@JoinColumn(name = "username", referencedColumnName = "username", nullable = false,columnDefinition = "TEXT"),
            @JoinColumn(name = "hospital_id",referencedColumnName = "hospital_id",columnDefinition = "bigint not null")}
    )
    private UserGroup userGroup;

    @Column(
            name = "date",
            nullable = false,
            columnDefinition = "DATE DEFAULT CURRENT_DATE"
    )
    private LocalDate date;

    @Column(
            name = "counter",
            columnDefinition = "bigint not null"
    )
    private long viewCounter;

    @Version
    private long version;

    public Viewing( Equipment equipmentId, LocalDate date, UserGroup userGroup) {
        this.viewingId = UUID.randomUUID().toString();
        this.equipmentId = equipmentId;
        this.date = date;
        this.userGroup = userGroup;
        this.viewCounter = 1L;
    }


    public Viewing() {

    }

    public long getVersion() {
        return version;
    }

    public String getViewingId()
    {
        return viewingId;
    }

    public void setViewingId(String viewingId)
    {
        this.viewingId = viewingId;
    }

    public Equipment getEquipmentId()
    {
        return equipmentId;
    }

    public void setEquipmentId(Equipment equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public void setViewCounter(long viewCounter) {
        this.viewCounter = viewCounter;
    }

    public long getViewCounter() {
        return viewCounter;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
