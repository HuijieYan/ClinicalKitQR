package ClinicalKitQR.equipment.services;

import ClinicalKitQR.equipment.models.Manufacturer;
import ClinicalKitQR.equipment.repositories.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {
    private final ManufacturerRepository repository;

    @Autowired
    public ManufacturerService(ManufacturerRepository repository){
        this.repository = repository;
    }

    public Manufacturer getByName(String name){
        return repository.findByName(name);
    }

    public List<Manufacturer> getAll(){
        return repository.getAll();
    }

    public void save(Manufacturer manufacturer){
        repository.save(manufacturer);
    }

    public void delete(String name){
        //this function is for testing only
        //this clears up the testing data
        repository.deleteByName(name);
    }
}
