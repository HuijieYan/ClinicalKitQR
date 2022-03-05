package ClinicalKitQR.equipment.services;

import ClinicalKitQR.equipment.models.EquipmentModel;
import ClinicalKitQR.equipment.repositories.EquipmentModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentModelService {
    private EquipmentModelRepository repository;

    @Autowired
    public EquipmentModelService(EquipmentModelRepository repository){
        this.repository = repository;
    }

    public List<EquipmentModel> getModelsByHospital(long hospitalId){
        return repository.findByHospitalId(hospitalId);
    }

    public List<EquipmentModel> getModelsByHospitalAndManufacture(long hospitalId,String manufacture){
        return repository.findByHospitalIdAndManufacturer(hospitalId,manufacture);
    }

    public List<EquipmentModel> getModelsByTrust(long trustId){
        return repository.findByTrustId(trustId);
    }

    public List<EquipmentModel> getModelsByTrustAndManufacture(long trustId,String manufacture){
        return repository.findByTrustIdAndManufacturer(trustId,manufacture);
    }
}
