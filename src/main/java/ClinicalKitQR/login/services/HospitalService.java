package ClinicalKitQR.login.services;

import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.repositories.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return repository.save(hospital);
    }

    public List<Hospital> getAllByTrust(long id){
        List<Hospital> hospitals = new ArrayList<>();
        Hospital trustAdmin = repository.findTrustAdmin(id,"Trust Admin");
        if(trustAdmin != null){
            hospitals.add(trustAdmin);
        }
        //order the list so trust admin will always be the first hospital in the list
        hospitals.addAll(repository.findByTrustId(id,"Trust Admin"));
        return hospitals;
    }

    public List<Hospital> getAll(){
        return repository.getAll();
    }

    public List<Hospital> findAll(){
        return repository.findAll();
    }

    public Hospital findByID(long id){
        return repository.findByHospitalId(id);
    }

    public void delete(long id){
        repository.deleteByPK(id);
    }

    public void update(long id,String name){
        repository.update(id,name);
    }

}
