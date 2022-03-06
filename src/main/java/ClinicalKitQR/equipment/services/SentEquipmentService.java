package ClinicalKitQR.equipment.services;

import ClinicalKitQR.equipment.models.SentEquipment;
import ClinicalKitQR.equipment.repositories.SentEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SentEquipmentService {
    public SentEquipmentRepository repository;

    @Autowired
    public SentEquipmentService(SentEquipmentRepository repository){
        this.repository = repository;
    }

    public void save(SentEquipment sentEquipment){
        sentEquipment.getHospitalId().addSentEquipment(sentEquipment);
        repository.save(sentEquipment);
    }

    public void delete(String id){
        repository.deleteById(id);
    }

    public SentEquipment getById(String id){
        return repository.getById(id);
    }

    public void updateSaved(String id){
        repository.updateSaved(id);
    }
}