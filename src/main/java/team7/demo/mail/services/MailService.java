package team7.demo.mail.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.mail.models.Mail;
import team7.demo.mail.repositories.MailRepository;

import java.util.List;

@Service
public class MailService {
    private final MailRepository repository;

    @Autowired
    public MailService(MailRepository repository){
        this.repository = repository;
    }

    public List<Mail>  getAllReceived(long id,String username){
        return repository.getAllReceived(id,username);
    }

    public void save(Mail mail){
        repository.save(mail);
    }
}
