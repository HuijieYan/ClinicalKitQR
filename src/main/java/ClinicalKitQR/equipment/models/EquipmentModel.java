package ClinicalKitQR.equipment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Locale;
import java.util.UUID;

/**
 * The model of an equipment i.e. UX-001 for equipment with model name UX-001
 *
 * @value equipment uses the equipment id to uniquely identify itself
 * @value modelName the name of this model
 * @value manufacturer the manufacturer of this equipment's model
 */

@Entity
@Table
public class EquipmentModel {
    @Id
    private String modelId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "model")
    private Equipment equipment;

    private String modelName;

    private String modelSearchName;

    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    @JoinColumn(
            name = "manufacturer",
            referencedColumnName = "manufacturerName"
    )
    private Manufacturer manufacturer;

    public EquipmentModel(){}

    public EquipmentModel(EquipmentModel model){
        this.modelId = UUID.randomUUID().toString();
        this.modelName = model.getModelName();
        this.manufacturer = model.getManufacturer();
        this.modelSearchName = model.getModelSearchName();
        manufacturer.addModel(this);
    }

    public EquipmentModel(String modelName,Manufacturer manufacturer){
        this.modelId = UUID.randomUUID().toString();
        this.modelName = modelName;
        this.modelSearchName = modelName.replaceAll(" ","").toLowerCase();
        this.manufacturer = manufacturer;
        manufacturer.addModel(this);
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
        this.modelSearchName = modelName.replaceAll(" ","").toLowerCase();
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelId() {
        return modelId;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModelSearchName() {
        return modelSearchName;
    }
}
