package team7.demo.login.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.login.models.Specialty;
import team7.demo.login.repositories.SpecialtyRepository;

import java.util.List;

@Service
public class SpecialtyService {
    private final SpecialtyRepository repository;

    @Autowired
    public SpecialtyService(SpecialtyRepository repository){
        this.repository = repository;
    }

    public Specialty findByName(String name){
        return repository.findBySpecialty(name);
    }

    public List<Specialty> getAll(){
        return repository.findAll();
    }

    public void save(Specialty specialty){
        repository.save(specialty);
    }
}
