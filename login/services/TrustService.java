package ClinicalKitQR.login.services;

import ClinicalKitQR.login.models.Trust;
import ClinicalKitQR.login.repositories.TrustRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrustService {
    private final TrustRepository repository;

    @Autowired
    public TrustService(TrustRepository repository){
        this.repository = repository;
    }

    public Trust findByID(long id){
        return repository.findByTrustId(id);
    }

    public List<Trust> getAll(){
        return repository.findAll();
    }

    public void save(Trust trust){
        repository.saveAndFlush(trust);
    }

    public void delete(long id){
        Trust trust = repository.findByTrustId(id);
        repository.deleteByPK(id);
    }
}
