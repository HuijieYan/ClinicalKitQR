package ClinicalKitQR.login.services;

import ClinicalKitQR.login.models.Hospital;
import ClinicalKitQR.login.repositories.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String getNameByID(long id){
        return findByID(id).getHospitalName();
    }

    public void delete(long id){
        repository.deleteByPK(id);
    }

    public void update(long id,String name){
        repository.update(id,name);
    }

}
