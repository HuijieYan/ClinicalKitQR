package ClinicalKitQR.equipment.services;

import ClinicalKitQR.equipment.models.SentEquipment;
import ClinicalKitQR.equipment.repositories.SentEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SentEquipmentService {
    public SentEquipmentRepository repository;
    //no save function because SentEquipment is saved when saving Mails by cascade

    @Autowired
    public SentEquipmentService(SentEquipmentRepository repository){
        this.repository = repository;
    }

    public SentEquipment getById(String id){
        return repository.getById(id);
    }

    public void updateSaved(String id){
        repository.updateSaved(id);
    }

    public List<SentEquipment> getAll(){
        return repository.findAll();
    }
}
