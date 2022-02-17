package team7.demo.equipment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.Constant;
import team7.demo.equipment.models.SentEquipment;
import team7.demo.equipment.repositories.EquipmentRepository;
import team7.demo.equipment.repositories.SentEquipmentRepository;
import team7.demo.issue.repositories.IssueRepository;

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
