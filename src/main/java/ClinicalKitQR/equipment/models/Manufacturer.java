package ClinicalKitQR.equipment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the company that make equipment
 *
 * @value name the name of the company, all companies' name shall be unique
 * @value models stores the equipment models that this company made
 */

@Entity
@Table
public class Manufacturer {
    @Id
    private String manufacturerName;

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "manufacturer",orphanRemoval = true,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<EquipmentModel> models = new ArrayList<>();

    public Manufacturer(){

    }

    public Manufacturer(String name){
        this.manufacturerName = name;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String name) {
        this.manufacturerName = name;
    }

    public List<EquipmentModel> getModels() {
        return models;
    }

    public void removeModel(EquipmentModel model){
        int index = 0;
        for(int i =0;i<models.size();i++){
            EquipmentModel item = models.get(i);
            if(item.getModelId().equals(model.getModelId())){
                index = i;
            }
        }
        models.get(index).setManufacturer(null);
        model.setManufacturer(null);
        models.remove(index);
    }

    public void addModel(EquipmentModel model){
        model.setManufacturer(this);
        models.add(model);
    }
}
