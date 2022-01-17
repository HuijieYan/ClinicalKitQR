package team7.demo.mail.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.demo.mail.models.Mail;
import team7.demo.mail.models.MailPrimaryKey;

public interface MailRepository extends JpaRepository<Mail, MailPrimaryKey> {
}
