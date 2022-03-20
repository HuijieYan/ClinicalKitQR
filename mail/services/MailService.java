package ClinicalKitQR.mail.services;

import ClinicalKitQR.mail.models.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ClinicalKitQR.mail.repositories.MailRepository;

import java.util.List;

@Service
public class MailService {
    private final MailRepository repository;

    @Autowired
    public MailService(MailRepository repository){
        this.repository = repository;
    }

    public Mail getByPK(String id){
        return repository.get(id);
    }

    public List<Mail> getAllReceived(long id,String username){
        return repository.getAllReceived(id,username);
    }

    public List<Mail> getAllSent(long id,String username){
        return repository.getAllSent(id,username);
    }

    public void save(Mail mail){
        repository.save(mail);
    }

    public void delete(String id){
        repository.deleteByPK(id);
    }

    public List<Mail> getAll(){
        return repository.findAll();
    }

}
