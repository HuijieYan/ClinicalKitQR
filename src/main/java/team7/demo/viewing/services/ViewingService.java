package team7.demo.viewing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.equipment.models.Equipment;
import team7.demo.login.models.UserGroup;
import team7.demo.viewing.models.Viewing;
import team7.demo.viewing.repositories.ViewingRepository;

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

    public List<Viewing> getAllByEquipment(Long equipment) {
        return repository.getAllByEquipmentId(equipment);
    }

    public  List<Viewing> getAllByDate(LocalDate date) {
        return repository.getAllByDate(date);
    }

    public void save(Viewing viewing) {
        viewing.getEquipmentId().addViewing(viewing);
        viewing.getUserGroup().addViewing(viewing);
        System.out.println("Viewing:" + viewing + " saved at" + LocalDate.now());
        repository.save(viewing);
    }

    public List<Viewing> getAllByUserGroup(Long hospitalId, String username) {
        return repository.getAllByUserGroup(hospitalId, username);
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

}
