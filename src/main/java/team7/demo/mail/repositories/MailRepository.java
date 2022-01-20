package team7.demo.mail.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team7.demo.mail.models.Mail;
import team7.demo.mail.models.MailPrimaryKey;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, MailPrimaryKey> {
    @Query("select m from Mail m where m.receiver.hospitalId = ?1 and m.receiver.username = ?2")
    List<Mail> getAllReceived(long id,String username);
}
