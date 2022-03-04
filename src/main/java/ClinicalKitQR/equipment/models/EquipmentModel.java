package ClinicalKitQR.equipment.models;

import javax.persistence.*;
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

    @OneToOne(orphanRemoval = true,mappedBy = "model")
    private Equipment equipment;

    private String modelName;

    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
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
    }

    public EquipmentModel(String modelName){
        this.modelId = UUID.randomUUID().toString();
        this.modelName = modelName;
    }

    public EquipmentModel(String modelName,Manufacturer manufacturer){
        this.modelId = UUID.randomUUID().toString();
        this.modelName = modelName;
        manufacturer.addModel(this);
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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
}
