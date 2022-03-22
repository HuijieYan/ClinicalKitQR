package ClinicalKitQR.viewing.models;

import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.login.models.UserGroup;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents number of views of a user group that views equipment in one day,
 * admins are not counted
 *
 * @value viewingId an uuid that uniquely identifies the viewing
 * @value equipmentId the equipment that the user group viewed
 * @value userGroup the user group that viewed equipment
 * @value date the day when the user group viewed the equipment
 * @value viewCounter number of times this group viewed the equipment
 * @value version used to avoid concurrent update
 */
@Entity(name = "Viewing")
public class Viewing
{
    @Id
    private String viewingId;

    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "equipment_id",
            nullable = false,
            referencedColumnName = "equipmentId"
    )
    private Equipment equipmentId;

    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER
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
