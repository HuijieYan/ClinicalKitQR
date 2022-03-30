package ClinicalKitQR.viewing.services;

import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.viewing.models.EquipmentViewing;
import ClinicalKitQR.viewing.models.Viewing;
import ClinicalKitQR.viewing.repositories.ViewingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ViewingService {
    private final ViewingRepository repository;

    @Autowired
    public ViewingService(ViewingRepository repository) {
        this.repository = repository;
    }

    public List<EquipmentViewing> getAllByEquipmentId(Long equipment) {
        return repository.getAllByEquipmentId(equipment);
    }

    public List<EquipmentViewing> getAllByEquipmentIdAndDateBetween(Long equipment, LocalDate startDate, LocalDate endDate) {
        return repository.getAllByEquipmentIdAndDateBetween(equipment, startDate, endDate);
    }

    public List<EquipmentViewing> getAllByEquipmentIdAndDateBefore(Long equipment, LocalDate endDate) {
        return repository.getAllByEquipmentIdAndDateBefore(equipment,endDate);
    }

    public void addNewView(Equipment equipment,LocalDate date,UserGroup group){
        Viewing view = repository.getViewToUpdate(equipment.getEquipmentId(),date,group);
        if (view!=null){
            long views = view.getViewCounter();
            long version = view.getVersion();
            boolean done = false;
            while (!done){
                try {
                    repository.incrementCounter(view.getViewingId(),views+1L,version);
                    done = true;
                }catch (OptimisticLockException oe){
                    try {
                        wait(100);
                        //wait 100ms before retry
                    }catch (Exception e){}
                }
            }


        }else{
            view = new Viewing(equipment,date,group);
            repository.save(view);
        }

    }

    public List<EquipmentViewing> getAllByEquipmentIdAndDateAfter(Long equipmentId, LocalDate startDate) {
        return repository.getAllByEquipmentIdAndDateAfter(equipmentId, startDate);
    }
}
