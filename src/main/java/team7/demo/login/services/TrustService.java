package team7.demo.login.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.login.models.Trust;
import team7.demo.login.repositories.TrustRepository;

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
}
