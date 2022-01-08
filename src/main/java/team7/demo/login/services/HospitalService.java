package team7.demo.login.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.login.models.Hospital;
import team7.demo.login.repositories.HospitalRepository;

import java.util.List;

@Service
public class HospitalService {
    private final HospitalRepository repository;

    @Autowired
    //automatically assign repo
    public HospitalService(HospitalRepository repository){
        this.repository = repository;
    }

    public Hospital save(Hospital hospital){
        hospital.getTrust().addHospital(hospital);
        return repository.save(hospital);
    }

    public List<Hospital> getAllByTrust(long id){
        return repository.findByTrustId(id);
    }

    public List<Hospital> getAll(){
        return repository.findAll();
    }

    public Hospital findByID(long id){
        return repository.findByHospitalId(id);
    }
}
