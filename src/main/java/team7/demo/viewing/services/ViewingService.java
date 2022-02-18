package team7.demo.viewing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.equipment.models.Equipment;
import team7.demo.login.models.UserGroup;
import team7.demo.viewing.models.Viewing;
import team7.demo.viewing.repositories.ViewingRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ViewingService {
    private final ViewingRepository repository;

    @Autowired
    public ViewingService(ViewingRepository repository) {
        this.repository = repository;
    }

    public List<Viewing> getAllByEquipment(Equipment equipment) {
        return repository.getAllByEquipmentId(equipment);
    }

    public  List<Viewing> getAllByDate(LocalDate date) {
        return repository.getAllByDate(date);
    }

    public void save(Viewing viewing) {
        viewing.getEquipmentId().addViewing(viewing);
        viewing.getUserGroup().addViewing(viewing);
        repository.save(viewing);
    }

    public List<Viewing> getAllByUserGroup(UserGroup userGroup) {
        return repository.getAllByUserGroup(userGroup);
    }

}
