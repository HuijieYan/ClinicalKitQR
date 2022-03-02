package ClinicalKitQR.mail.repositories;

import ClinicalKitQR.mail.models.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface MailRepository extends JpaRepository<Mail, String> {
    @Query("select m from Mail m join m.receiver r where r.hospitalId.hospitalId = ?1 and r.username = ?2 order by m.time DESC ")
    List<Mail> getAllReceived(long id,String username);

    @Query("select m from Mail m where m.senderHospitalId = ?1 and m.senderUsername = ?2 and m.receiver is null order by m.time DESC")
    List<Mail> getAllSent(long id,String username);

    @Query("select m from Mail m where m.id =?1 ")
    Mail get(String id);

    @Transactional
    @Modifying
    @Query("delete from Mail m where m.id = ?1")
    void deleteByPK(String id);

}
