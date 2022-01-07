package team7.demo.equipment.models;

import java.io.Serializable;
import java.util.Objects;

public class EquipmentPrimaryKey implements Serializable {
    private long hospitalId;
    private long equipmentId;

    public EquipmentPrimaryKey(){}

    public EquipmentPrimaryKey(long hospitalId,long equipmentId){
        this.equipmentId = equipmentId;
        this.hospitalId = hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public long getHospitalId() {
        return hospitalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquipmentPrimaryKey)) return false;
        EquipmentPrimaryKey that = (EquipmentPrimaryKey) o;
        return getHospitalId() == that.getHospitalId() && getEquipmentId() == that.getEquipmentId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHospitalId(), getEquipmentId());
    }
}
